package com.example.tournament.controller;

import com.example.tournament.service.InningsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.*;

public class InningsControllerTest {

    @InjectMocks
    private InningsController inningsController;

    @Mock
    private InningsService inningsService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testTossDecision_Success() throws InterruptedException {
        // Arrange
        int mid = 1;

        // Act
        inningsController.tossDecision(mid);

        // Assert
        verify(inningsService).startMatchWithTossAndSetup(mid);  // Verify that startMatch was called with the correct argument
    }
}