Feature: Verify user login

  Scenario: Successful login
    Given User launches mobile app
    When User enters username and password
    And User clicks on signin
    Then User verifies whether login is successful
