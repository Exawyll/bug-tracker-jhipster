import { browser, ExpectedConditions as ec, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../page-objects/jhi-page-objects';

import { OpportunityComponentsPage, OpportunityDeleteDialog, OpportunityUpdatePage } from './opportunity.page-object';

const expect = chai.expect;

describe('Opportunity e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let opportunityComponentsPage: OpportunityComponentsPage;
  let opportunityUpdatePage: OpportunityUpdatePage;
  let opportunityDeleteDialog: OpportunityDeleteDialog;

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing('admin', 'admin');
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load Opportunities', async () => {
    await navBarPage.goToEntity('opportunity');
    opportunityComponentsPage = new OpportunityComponentsPage();
    await browser.wait(ec.visibilityOf(opportunityComponentsPage.title), 5000);
    expect(await opportunityComponentsPage.getTitle()).to.eq('bugtrackerjhipsterApp.opportunity.home.title');
  });

  it('should load create Opportunity page', async () => {
    await opportunityComponentsPage.clickOnCreateButton();
    opportunityUpdatePage = new OpportunityUpdatePage();
    expect(await opportunityUpdatePage.getPageTitle()).to.eq('bugtrackerjhipsterApp.opportunity.home.createOrEditLabel');
    await opportunityUpdatePage.cancel();
  });

  it('should create and save Opportunities', async () => {
    const nbButtonsBeforeCreate = await opportunityComponentsPage.countDeleteButtons();

    await opportunityComponentsPage.clickOnCreateButton();
    await promise.all([
      opportunityUpdatePage.setCompanyNameInput('companyName'),
      opportunityUpdatePage.setPlaceInput('place'),
      opportunityUpdatePage.contactFromSelectLastOption()
    ]);
    expect(await opportunityUpdatePage.getCompanyNameInput()).to.eq(
      'companyName',
      'Expected CompanyName value to be equals to companyName'
    );
    expect(await opportunityUpdatePage.getPlaceInput()).to.eq('place', 'Expected Place value to be equals to place');
    await opportunityUpdatePage.save();
    expect(await opportunityUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await opportunityComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1, 'Expected one more entry in the table');
  });

  it('should delete last Opportunity', async () => {
    const nbButtonsBeforeDelete = await opportunityComponentsPage.countDeleteButtons();
    await opportunityComponentsPage.clickOnLastDeleteButton();

    opportunityDeleteDialog = new OpportunityDeleteDialog();
    expect(await opportunityDeleteDialog.getDialogTitle()).to.eq('bugtrackerjhipsterApp.opportunity.delete.question');
    await opportunityDeleteDialog.clickOnConfirmButton();

    expect(await opportunityComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
