Feature: Read states from Excel
  As a tester
  I want to read a column from Excel
  So that I can use the values in my tests

  Scenario: Load 'State Name' list
    Given I read the "Users" sheet column "State Name" from "testdata.xlsx"
    Then I should have 10 products

  @excel
  Scenario: Load 'State Name' list
    Given I read the "Sheet1" sheet column "First Name" from "sample_test_data_3_columns.xlsx"
    Then I should have 10000 products