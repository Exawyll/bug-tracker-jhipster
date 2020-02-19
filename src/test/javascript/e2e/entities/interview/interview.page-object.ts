import { element, by, ElementFinder } from 'protractor';

export class InterviewComponentsPage {
  createButton = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('jhi-interview div table .btn-danger'));
  title = element.all(by.css('jhi-interview div h2#page-heading span')).first();

  async clickOnCreateButton(): Promise<void> {
    await this.createButton.click();
  }

  async clickOnLastDeleteButton(): Promise<void> {
    await this.deleteButtons.last().click();
  }

  async countDeleteButtons(): Promise<number> {
    return this.deleteButtons.count();
  }

  async getTitle(): Promise<string> {
    return this.title.getAttribute('jhiTranslate');
  }
}

export class InterviewUpdatePage {
  pageTitle = element(by.id('jhi-interview-heading'));
  saveButton = element(by.id('save-entity'));
  cancelButton = element(by.id('cancel-save'));
  occuredDateInput = element(by.id('field_occuredDate'));
  jobTitleInput = element(by.id('field_jobTitle'));
  descriptionInput = element(by.id('field_description'));
  salaryInput = element(by.id('field_salary'));
  contactInput = element(by.id('field_contact'));
  typeSelect = element(by.id('field_type'));
  opportunitySelect = element(by.id('field_opportunity'));

  async getPageTitle(): Promise<string> {
    return this.pageTitle.getAttribute('jhiTranslate');
  }

  async setOccuredDateInput(occuredDate: string): Promise<void> {
    await this.occuredDateInput.sendKeys(occuredDate);
  }

  async getOccuredDateInput(): Promise<string> {
    return await this.occuredDateInput.getAttribute('value');
  }

  async setJobTitleInput(jobTitle: string): Promise<void> {
    await this.jobTitleInput.sendKeys(jobTitle);
  }

  async getJobTitleInput(): Promise<string> {
    return await this.jobTitleInput.getAttribute('value');
  }

  async setDescriptionInput(description: string): Promise<void> {
    await this.descriptionInput.sendKeys(description);
  }

  async getDescriptionInput(): Promise<string> {
    return await this.descriptionInput.getAttribute('value');
  }

  async setSalaryInput(salary: string): Promise<void> {
    await this.salaryInput.sendKeys(salary);
  }

  async getSalaryInput(): Promise<string> {
    return await this.salaryInput.getAttribute('value');
  }

  async setContactInput(contact: string): Promise<void> {
    await this.contactInput.sendKeys(contact);
  }

  async getContactInput(): Promise<string> {
    return await this.contactInput.getAttribute('value');
  }

  async setTypeSelect(type: string): Promise<void> {
    await this.typeSelect.sendKeys(type);
  }

  async getTypeSelect(): Promise<string> {
    return await this.typeSelect.element(by.css('option:checked')).getText();
  }

  async typeSelectLastOption(): Promise<void> {
    await this.typeSelect
      .all(by.tagName('option'))
      .last()
      .click();
  }

  async opportunitySelectLastOption(): Promise<void> {
    await this.opportunitySelect
      .all(by.tagName('option'))
      .last()
      .click();
  }

  async opportunitySelectOption(option: string): Promise<void> {
    await this.opportunitySelect.sendKeys(option);
  }

  getOpportunitySelect(): ElementFinder {
    return this.opportunitySelect;
  }

  async getOpportunitySelectedOption(): Promise<string> {
    return await this.opportunitySelect.element(by.css('option:checked')).getText();
  }

  async save(): Promise<void> {
    await this.saveButton.click();
  }

  async cancel(): Promise<void> {
    await this.cancelButton.click();
  }

  getSaveButton(): ElementFinder {
    return this.saveButton;
  }
}

export class InterviewDeleteDialog {
  private dialogTitle = element(by.id('jhi-delete-interview-heading'));
  private confirmButton = element(by.id('jhi-confirm-delete-interview'));

  async getDialogTitle(): Promise<string> {
    return this.dialogTitle.getAttribute('jhiTranslate');
  }

  async clickOnConfirmButton(): Promise<void> {
    await this.confirmButton.click();
  }
}
