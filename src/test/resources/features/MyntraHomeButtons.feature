@MyntraFeature
Feature: Myntra Home page buttons

  @MyntraScenario
  Scenario Outline: Validate home page buttons.
    Given User is on home page
    When User clicks on Categories One
    When User clicks Studio <index> Two
    Examples:
      | index |
      | 0     |