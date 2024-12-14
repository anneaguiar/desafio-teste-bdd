Feature: Google Search Functionality
  Scenario: Search for a term on Google
    Given I open the Google homepage
    When I search for "SRE Bootcamp"
    Then I should see results containing "SRE Bootcamp"