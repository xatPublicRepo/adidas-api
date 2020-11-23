@test
Feature: Verify DEMO PET successfully

  Scenario: Verify get, add, update, delete pet

#    Get "available" pets -> API is stuck on loading...

    Given I add new pet via API
      | pet_type   | dog       |
      | pet_name   | maggie    |
      | pet_status | available |

    Then I verify the new pet added

    And I update the pet status to sold
      | pet_type   | dog    |
      | pet_name   | maggie |
      | pet_status | sold   |

    Then I verify the new pet status updated

    And I delete the pet and verify
