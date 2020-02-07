import { browser, ExpectedConditions as ec, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../page-objects/jhi-page-objects';

import { SellingProductComponentsPage, SellingProductDeleteDialog, SellingProductUpdatePage } from './selling-product.page-object';

const expect = chai.expect;

describe('SellingProduct e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let sellingProductComponentsPage: SellingProductComponentsPage;
  let sellingProductUpdatePage: SellingProductUpdatePage;
  let sellingProductDeleteDialog: SellingProductDeleteDialog;

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing('admin', 'admin');
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load SellingProducts', async () => {
    await navBarPage.goToEntity('selling-product');
    sellingProductComponentsPage = new SellingProductComponentsPage();
    await browser.wait(ec.visibilityOf(sellingProductComponentsPage.title), 5000);
    expect(await sellingProductComponentsPage.getTitle()).to.eq('bugtrackerjhipsterApp.sellingProduct.home.title');
  });

  it('should load create SellingProduct page', async () => {
    await sellingProductComponentsPage.clickOnCreateButton();
    sellingProductUpdatePage = new SellingProductUpdatePage();
    expect(await sellingProductUpdatePage.getPageTitle()).to.eq('bugtrackerjhipsterApp.sellingProduct.home.createOrEditLabel');
    await sellingProductUpdatePage.cancel();
  });

  it('should create and save SellingProducts', async () => {
    const nbButtonsBeforeCreate = await sellingProductComponentsPage.countDeleteButtons();

    await sellingProductComponentsPage.clickOnCreateButton();
    await promise.all([
      sellingProductUpdatePage.setV2ProductNameInput('v2ProductName'),
      sellingProductUpdatePage.setV2ProductCategoryInput('v2ProductCategory'),
      sellingProductUpdatePage.setUnitsSoldInput('5'),
      sellingProductUpdatePage.setRevenueInput('5')
    ]);
    expect(await sellingProductUpdatePage.getV2ProductNameInput()).to.eq(
      'v2ProductName',
      'Expected V2ProductName value to be equals to v2ProductName'
    );
    expect(await sellingProductUpdatePage.getV2ProductCategoryInput()).to.eq(
      'v2ProductCategory',
      'Expected V2ProductCategory value to be equals to v2ProductCategory'
    );
    expect(await sellingProductUpdatePage.getUnitsSoldInput()).to.eq('5', 'Expected unitsSold value to be equals to 5');
    expect(await sellingProductUpdatePage.getRevenueInput()).to.eq('5', 'Expected revenue value to be equals to 5');
    await sellingProductUpdatePage.save();
    expect(await sellingProductUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await sellingProductComponentsPage.countDeleteButtons()).to.eq(
      nbButtonsBeforeCreate + 1,
      'Expected one more entry in the table'
    );
  });

  it('should delete last SellingProduct', async () => {
    const nbButtonsBeforeDelete = await sellingProductComponentsPage.countDeleteButtons();
    await sellingProductComponentsPage.clickOnLastDeleteButton();

    sellingProductDeleteDialog = new SellingProductDeleteDialog();
    expect(await sellingProductDeleteDialog.getDialogTitle()).to.eq('bugtrackerjhipsterApp.sellingProduct.delete.question');
    await sellingProductDeleteDialog.clickOnConfirmButton();

    expect(await sellingProductComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
