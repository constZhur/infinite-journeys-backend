package ru.mirea.infinitejourneysbackend.unit.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageImpl;
import ru.mirea.infinitejourneysbackend.domain.dto.tour.TourFilter;
import ru.mirea.infinitejourneysbackend.domain.dto.tour.TourRequest;
import ru.mirea.infinitejourneysbackend.domain.dto.tour.UpdateTourPriceRequest;
import ru.mirea.infinitejourneysbackend.domain.dto.tour.UpdateTourRequest;
import ru.mirea.infinitejourneysbackend.domain.model.*;
import ru.mirea.infinitejourneysbackend.exception.user.ForbiddenAccessProblem;
import ru.mirea.infinitejourneysbackend.repository.TourFileRelationRepository;
import ru.mirea.infinitejourneysbackend.repository.TourRepository;
import ru.mirea.infinitejourneysbackend.service.CountryService;
import ru.mirea.infinitejourneysbackend.service.FileService;
import ru.mirea.infinitejourneysbackend.service.TourService;
import ru.mirea.infinitejourneysbackend.service.UserService;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TourServiceTest {

    @Mock
    private TourRepository repository;

    @Mock
    private FileService fileService;

    @Mock
    private CountryService countryService;

    @Mock
    private UserService userService;

    @Mock
    private TourFileRelationRepository tourFileRelationRepository;

    @Spy
    @InjectMocks
    private TourService tourService;

    @Test
    void testCreateTour() {
        User currentUser = mock(User.class);
        Country country = mock(Country.class);
        OffsetDateTime startDate = OffsetDateTime.parse("2023-05-05T10:10:10.000+00:00");
        OffsetDateTime endDate = OffsetDateTime.parse("2023-05-10T10:10:10.000+00:00");

        when(userService.getCurrentUser()).thenReturn(currentUser);
        when(countryService.getById(anyLong())).thenReturn(country);

        TourRequest request = mock(TourRequest.class);
        when(request.title()).thenReturn("Test Tour");
        when(request.description()).thenReturn("Description");
        when(request.startDate()).thenReturn(startDate);
        when(request.endDate()).thenReturn(endDate);
        when(request.price()).thenReturn(100.0);
        when(request.countryId()).thenReturn(1L);

        Tour tour = new Tour();
        tour.setTitle("Test Tour");
        tour.setDescription("Description");
        tour.setStartDate(startDate);
        tour.setEndDate(endDate);
        tour.setPrice(100.0);
        tour.setCountry(country);
        tour.setSeller(currentUser);
        when(repository.save(any(Tour.class))).thenReturn(tour);

        Tour createdTour = tourService.create(request);

        verify(repository).save(any(Tour.class));
        assertThat(createdTour.getTitle()).isEqualTo("Test Tour");
        assertThat(createdTour.getDescription()).isEqualTo("Description");
        assertThat(createdTour.getStartDate()).isEqualTo(startDate);
        assertThat(createdTour.getEndDate()).isEqualTo(endDate);
        assertThat(createdTour.getPrice()).isEqualTo(100.0);
        assertThat(createdTour.getCountry()).isEqualTo(country);
        assertThat(createdTour.getSeller()).isEqualTo(currentUser);
    }

    @Test
    void testDeleteTour() {
        User currentUser = mock(User.class);
        when(userService.getCurrentUser()).thenReturn(currentUser);

        Tour tour = mock(Tour.class);
        when(tour.isSeller(currentUser)).thenReturn(true);
        when(tour.getAttachments()).thenReturn(Collections.emptyList());
        when(repository.findById(anyLong())).thenReturn(Optional.of(tour));

        tourService.deleteById(1L);

        verify(repository).deleteById(1L);
    }

    @Test
    void testDeleteTourForbidden() {
        User currentUser = mock(User.class);
        when(userService.getCurrentUser()).thenReturn(currentUser);

        Tour tour = mock(Tour.class);
        when(tour.isSeller(currentUser)).thenReturn(false);
        when(currentUser.isAdmin()).thenReturn(false);
        when(repository.findById(anyLong())).thenReturn(Optional.of(tour));

        assertThrows(ForbiddenAccessProblem.class, () -> tourService.deleteById(1L));

        verify(repository, never()).deleteById(anyLong());
    }

    @Test
    void testUpdateTour() {
        User currentUser = mock(User.class);
        Country country = mock(Country.class);
        when(userService.getCurrentUser()).thenReturn(currentUser);
        when(countryService.getById(anyLong())).thenReturn(country);

        Tour tour = mock(Tour.class);
        when(tour.isSeller(currentUser)).thenReturn(true);
        when(tour.getAttachments()).thenReturn(Collections.emptyList());
        when(repository.findById(anyLong())).thenReturn(Optional.of(tour));

        UpdateTourRequest request = mock(UpdateTourRequest.class);
        when(request.title()).thenReturn("Updated Title");
        when(request.description()).thenReturn("Updated Description");
        when(request.countryId()).thenReturn(1L);

        tourService.update(request, 1L);

        verify(tour).setTitle("Updated Title");
        verify(tour).setDescription("Updated Description");
        verify(tour).setCountry(country);
        verify(repository).save(tour);
    }

    @Test
    void testFindByFilter() {
        TourFilter filter = mock(TourFilter.class);
        when(filter.getPage()).thenReturn(0);
        when(filter.getSize()).thenReturn(10);
        when(filter.getCountryId()).thenReturn(1L);
        when(filter.getTourName()).thenReturn("Tour");

        Pageable pageable = PageRequest.of(0, 10);
        List<Tour> tours = Collections.singletonList(mock(Tour.class));
        Page<Tour> page = new PageImpl<>(tours, pageable, 1);

        when(repository.findAllByCountryIdAndTourName(1L, "Tour", pageable)).thenReturn(page);

        Page<Tour> result = tourService.findByFilter(filter);

        assertThat(result.getContent()).isEqualTo(tours);
    }

    @Test
    void testGetAllTours() {
        List<Tour> tours = List.of(
                new Tour(),
                new Tour()
        );

        when(repository.findAll()).thenReturn(tours);

        List<Tour> result = tourService.getAll();

        assertThat(result).isEqualTo(tours);
        verify(repository).findAll();
    }

    @Test
    void testDeleteBySellerId() {
        UUID sellerId = UUID.randomUUID();

        List<Tour> tours = List.of(
                Tour.builder()
                        .id(1L)
                        .title("Первый тур")
                        .description("Информация о первом туре")
                        .build(),
                Tour.builder()
                        .id(2L)
                        .title("Второй тур")
                        .description("Информация о втором туре")
                        .build()
        );

        doNothing().when(tourService).deleteById(anyLong());

        when(repository.findAllBySellerId(sellerId)).thenReturn(tours);

        tourService.deleteBySellerId(sellerId);

        verify(repository, times(1)).findAllBySellerId(sellerId);
        verify(tourService, times(tours.size())).deleteById(anyLong());
    }

    @Test
    void testUpdateTourPrice() {
        User currentUser = mock(User.class);
        when(userService.getCurrentUser()).thenReturn(currentUser);

        Tour tour = mock(Tour.class);
        when(tour.isSeller(currentUser)).thenReturn(true);
        when(repository.findById(anyLong())).thenReturn(Optional.of(tour));

        UpdateTourPriceRequest request = new UpdateTourPriceRequest(150.00);

        tourService.updateTourPrice(request, 1L);

        verify(tour).setPrice(request.price());
        verify(repository).save(tour);
    }

    @Test
    void testUpdateTourPriceForbidden() {
        User currentUser = mock(User.class);
        lenient().when(userService.getCurrentUser()).thenReturn(currentUser);

        Tour tour = mock(Tour.class);
        when(tour.isSeller(currentUser)).thenReturn(false);
        lenient().when(currentUser.isAdmin()).thenReturn(false);
        when(repository.findById(anyLong())).thenReturn(Optional.of(tour));

        UpdateTourPriceRequest request = new UpdateTourPriceRequest(150.00);

        assertThrows(ForbiddenAccessProblem.class, () -> tourService.updateTourPrice(request, 1L));

        verify(tour, never()).setPrice(any(Double.class));
        verify(repository, never()).save(tour);
    }

    @Test
    void testUpdateTourAttachmentsForbidden() {
        User currentUser = mock(User.class);
        when(userService.getCurrentUser()).thenReturn(currentUser);

        Tour tour = mock(Tour.class);

        UpdateTourRequest request = new UpdateTourRequest(
                "title", "description", OffsetDateTime.now(),
                OffsetDateTime.now().plusDays(7), null, null, null
        );

        when(repository.findById(anyLong())).thenReturn(Optional.of(tour));

        assertThrows(ForbiddenAccessProblem.class, () -> tourService.update(request, 1L));

        verify(repository, never()).save(any(Tour.class));
    }


    @Test
    void testGetAll() {
        List<Tour> tours = List.of(
                Tour.builder().id(1L).build(),
                Tour.builder().id(2L).build()
        );
        when(repository.findAll()).thenReturn(tours);

        List<Tour> allTours = tourService.getAll();

        assertThat(tours.size()).isEqualTo(allTours.size());
        verify(repository).findAll();
    }

    @Test
    void testUpdateTourThrowsForbiddenAccessProblemForDeletedAttachments() {
        Long tourId = 1L;
        Long countryId = 2L;
        UUID sellerId = UUID.randomUUID();
        UUID nonExistentFileId = UUID.randomUUID();

        User currentUser = mock(User.class);
        Country country = mock(Country.class);
        UploadedFile file = mock(UploadedFile.class);
        when(file.getId()).thenReturn(UUID.randomUUID());

        TourFileRelation relation = mock(TourFileRelation.class);
        when(relation.getFile()).thenReturn(file);

        Tour tour = new Tour();
        tour.setSeller(currentUser);
        tour.setAttachments(List.of(relation));

        UpdateTourRequest request = mock(UpdateTourRequest.class);
        when(request.deletedAttachments()).thenReturn(List.of(nonExistentFileId));
        when(request.countryId()).thenReturn(countryId);

        when(currentUser.getId()).thenReturn(sellerId);
        when(repository.findById(tourId)).thenReturn(Optional.of(tour));
        when(userService.getCurrentUser()).thenReturn(currentUser);
        when(countryService.getById(countryId)).thenReturn(country);

        assertThrows(ForbiddenAccessProblem.class, () -> tourService.update(request, tourId));
    }
}
