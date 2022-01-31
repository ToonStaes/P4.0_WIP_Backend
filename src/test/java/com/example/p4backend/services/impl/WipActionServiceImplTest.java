package com.example.p4backend.services.impl;

import com.example.p4backend.models.Action;
import com.example.p4backend.models.complete.CompleteAction;
import com.example.p4backend.repositories.*;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
//import static org.mockito.Matchers.any;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@UnitTest
@RunWith(MockitoJUnitRunner.class)
class WipActionServiceImplTest {

    @Mock
    private ActionRepository actionRepository;
    @Mock
    private VzwRepository vzwRepository;
    @Mock
    private ActionImageRepository actionImageRepository;
    @Mock
    private ProductRepository productRepository;
    @Mock
    private PurchaseRepository purchaseRepository;
    @Mock
    private AddressRepository addressRepository;

    @InjectMocks
    private WipActionServiceImpl wipActionService;

    @Test
    void testGetNewestActions() {
        List<Action> actionList  = new ArrayList<>();

        when(actionRepository.findByEndDateAfterOrderByStartDateDesc(new Date())).thenReturn(actionList);
        doReturn(0).when(wipActionService).getProgress(Action);

        List<CompleteAction> result = wipActionService.getNewestActions(Boolean.TRUE);

        assertEquals(List<CompleteAction>, result);
    }
}