import { element, by, ElementFinder } from 'protractor';

export class OpportunityComponentsPage {
  createButton = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('jhi-opportunity div table .btn-danger'));
  title = element.all(by.css('jhi-opportunity div h2#page-heading span')).first();

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

export class OpportunityUpdatePage {
  pageTitle = element(by.id('jhi-opportunity-heading'));
  saveButton = element(by.id('save-entity'));
  cancelButton = element(by.id('cancel-save'));
  companyNameInput = element(by.id('field_companyName'));
  placeInput = element(by.id('field_place'));
  contactFromSelect = element(by.id('field_contactFrom'));

  async getPageTitle(): Promise<string> {
    return this.pageTitle.getAttribute('jhiTranslate');
  }

  async setCompanyNameInput(companyName: string): Promise<void> {
    await this.companyNameInput.sendKeys(companyName);
  }

  async getCompanyNameInput(): Promise<string> {
    return await this.companyNameInput.getAttribute('value');
  }

  async setPlaceInput(place: string): Promise<void> {
    await this.placeInput.sendKeys(place);
  }

  async getPlaceInput(): Promise<string> {
    return await this.placeInput.getAttribute('value');
  }

  async setContactFromSelect(contactFrom: string): Promise<void> {
    await this.contactFromSelect.sendKeys(contactFrom);
  }

  async getContactFromSelect(): Promise<string> {
    return await this.contactFromSelect.element(by.css('option:checked')).getText();
  }

  async contactFromSelectLastOption(): Promise<void> {
    await this.contactFromSelect
      .all(by.tagName('option'))
      .last()
      .click();
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

export class OpportunityDeleteDialog {
  private dialogTitle = element(by.id('jhi-delete-opportunity-heading'));
  private confirmButton = element(by.id('jhi-confirm-delete-opportunity'));

  async getDialogTitle(): Promise<string> {
    return this.dialogTitle.getAttribute('jhiTranslate');
  }

  async clickOnConfirmButton(): Promise<void> {
    await this.confirmButton.click();
  }
}
