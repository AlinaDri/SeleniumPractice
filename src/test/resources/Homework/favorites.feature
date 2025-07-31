Feature: Test Favorites functionality
    Test adding ads to Favorites list

    Scenario: Add an ad to Favorites via Categories
        Given user is in ss.com landing page
        When user navigates to category "Vehicles"
        And user finds an interesting ad
        And ads it to Favorites
        Then ad is successfully added to Favorites