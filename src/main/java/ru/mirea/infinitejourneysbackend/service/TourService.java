package ru.mirea.infinitejourneysbackend.service;


import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.mirea.infinitejourneysbackend.domain.dto.tour.TourFilter;
import ru.mirea.infinitejourneysbackend.domain.dto.tour.TourRequest;
import ru.mirea.infinitejourneysbackend.domain.dto.tour.UpdateTourPriceRequest;
import ru.mirea.infinitejourneysbackend.domain.dto.tour.UpdateTourRequest;
import ru.mirea.infinitejourneysbackend.domain.model.Tour;
import ru.mirea.infinitejourneysbackend.domain.model.User;
import ru.mirea.infinitejourneysbackend.exception.tour.TourNotFoundProblem;
import ru.mirea.infinitejourneysbackend.exception.user.ForbiddenAccessProblem;
import ru.mirea.infinitejourneysbackend.repository.TourFileRelationRepository;
import ru.mirea.infinitejourneysbackend.repository.TourRepository;

import java.util.Optional;
import java.util.UUID;

import static java.util.Objects.isNull;

@Service
@RequiredArgsConstructor
public class TourService {
    private final TourRepository repository;
    private final FileService fileService;
    private final CountryService countryService;
    private final UserService userService;
    private final TourFileRelationRepository tourFileRelationRepository;

    public Tour save(Tour tour) {
        return repository.save(tour);
    }

    @Transactional
    public Tour create(TourRequest request) {
        var tour = Tour.builder()
                .title(request.title())
                .seller(userService.getCurrentUser())
                .description(request.description())
                .startDate(request.startDate())
                .endDate(request.endDate())
                .price(request.price())
                .country(countryService.getById(request.countryId()))
                .build();
        return setTourInformation(request, tour);
    }

    public Optional<Tour> findById(Long id) {
        return repository.findById(id);
    }

    public Tour getById(Long tourId) {
        return findById(tourId).orElseThrow(() -> new TourNotFoundProblem(tourId.toString()));
    }

    @Transactional
    public void deleteById(Long tourId) {
        User currentUser = userService.getCurrentUser();
        Tour tour = getById(tourId);

        if (!tour.isSeller(currentUser) && !currentUser.isAdmin()) {
            throw new ForbiddenAccessProblem();
        }

        tour.getAttachments().forEach(rel -> {
            fileService.deleteById(rel.getFile().getId());
            tourFileRelationRepository.delete(rel);
        });

        repository.deleteById(tourId);
    }

    @Transactional
    public Tour update(UpdateTourRequest request, Long tourId) {
        var tour = getById(tourId);
        var currentUser = userService.getCurrentUser();
        var country = countryService.getById(request.countryId());

        if (!tour.isSeller(currentUser)) {
            throw new ForbiddenAccessProblem();
        }

        if (!isNull(request.deletedAttachments())) {
            request.deletedAttachments().forEach(fileId -> {
                if (tour.getAttachments().stream().noneMatch(rel -> rel.getFile().getId().equals(fileId))) {
                    throw new ForbiddenAccessProblem();
                }
            });
        }

        if (!isNull(request.deletedAttachments())) {
            tour.getAttachments().stream()
                    .filter(rel -> request.deletedAttachments().contains(rel.getFile().getId()))
                    .forEach(rel -> {
                        tour.getAttachments().remove(rel);
                        fileService.deleteById(rel.getFile().getId());
                    });
        }

        if (!isNull(request.attachments())) {
            tour.addFiles(fileService.uploadFiles(request.attachments()));
        }

        tour.setTitle(request.title());
        tour.setDescription(request.description());
        tour.setCountry(country);

        return save(tour);
    }

    private Tour setTourInformation(TourRequest request, Tour tour) {
        if (!isNull(request.attachments())) {
            tour.addFiles(fileService.uploadFiles(request.attachments()));
        }
        return save(tour);
    }

    public Page<Tour> findByFilter(TourFilter filter) {
        Pageable pageable = PageRequest.of(filter.getPage(), filter.getSize());
        return repository.findAllByCountryId(filter.getCountryId(), pageable);
    }

    @Transactional
    public void deleteBySellerId(UUID id) {
        var tours = repository.findAllBySellerId(id);
        tours.forEach(tour -> deleteById(tour.getId()));
    }

    @Transactional
    public Tour updateTourPrice(UpdateTourPriceRequest request, Long tourId) {
        var tour = getById(tourId);
        var currentUser = userService.getCurrentUser();

        if (!tour.isSeller(currentUser)) {
            throw new ForbiddenAccessProblem();
        }

        tour.setPrice(request.price());
        return save(tour);
    }
}
