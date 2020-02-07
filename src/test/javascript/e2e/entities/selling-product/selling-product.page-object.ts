import { element, by, ElementFinder } from 'protractor';

export class SellingProductComponentsPage {
  createButton = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('jhi-selling-product div table .btn-danger'));
  title = element.all(by.css('jhi-selling-product div h2#page-heading span')).first();

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

export class SellingProductUpdatePage {
  pageTitle = element(by.id('jhi-selling-product-heading'));
  saveButton = element(by.id('save-entity'));
  cancelButton = element(by.id('cancel-save'));
  v2ProductNameInput = element(by.id('field_v2ProductName'));
  v2ProductCategoryInput = element(by.id('field_v2ProductCategory'));
  unitsSoldInput = element(by.id('field_unitsSold'));
  revenueInput = element(by.id('field_revenue'));

  async getPageTitle(): Promise<string> {
    return this.pageTitle.getAttribute('jhiTranslate');
  }

  async setV2ProductNameInput(v2ProductName: string): Promise<void> {
    await this.v2ProductNameInput.sendKeys(v2ProductName);
  }

  async getV2ProductNameInput(): Promise<string> {
    return await this.v2ProductNameInput.getAttribute('value');
  }

  async setV2ProductCategoryInput(v2ProductCategory: string): Promise<void> {
    await this.v2ProductCategoryInput.sendKeys(v2ProductCategory);
  }

  async getV2ProductCategoryInput(): Promise<string> {
    return await this.v2ProductCategoryInput.getAttribute('value');
  }

  async setUnitsSoldInput(unitsSold: string): Promise<void> {
    await this.unitsSoldInput.sendKeys(unitsSold);
  }

  async getUnitsSoldInput(): Promise<string> {
    return await this.unitsSoldInput.getAttribute('value');
  }

  async setRevenueInput(revenue: string): Promise<void> {
    await this.revenueInput.sendKeys(revenue);
  }

  async getRevenueInput(): Promise<string> {
    return await this.revenueInput.getAttribute('value');
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

export class SellingProductDeleteDialog {
  private dialogTitle = element(by.id('jhi-delete-sellingProduct-heading'));
  private confirmButton = element(by.id('jhi-confirm-delete-sellingProduct'));

  async getDialogTitle(): Promise<string> {
    return this.dialogTitle.getAttribute('jhiTranslate');
  }

  async clickOnConfirmButton(): Promise<void> {
    await this.confirmButton.click();
  }
}
