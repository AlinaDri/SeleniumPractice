Feature: Test Favorites functionality
    Test adding ads to Favorites list

    Scenario: Add an ad to Favorites via Categories
        Given user is in ss.com landing page
        When user navigates to category "Transport"
        And user finds an interesting ad
        And ads it to Favorites
        Then ad is successfully added to Favorites

    Scenario: Add several ads to Favorites
        Given user is in ss.com landing page
        When user navigates to category "Real estate"
        And user finds three interesting ads
        And ads them to Favorites
        Then all ads are successfully added to Favorites