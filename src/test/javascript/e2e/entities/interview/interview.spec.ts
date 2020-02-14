import { browser, ExpectedConditions as ec, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../page-objects/jhi-page-objects';

import { InterviewComponentsPage, InterviewDeleteDialog, InterviewUpdatePage } from './interview.page-object';

const expect = chai.expect;

describe('Interview e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let interviewComponentsPage: InterviewComponentsPage;
  let interviewUpdatePage: InterviewUpdatePage;
  let interviewDeleteDialog: InterviewDeleteDialog;

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing('admin', 'admin');
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load Interviews', async () => {
    await navBarPage.goToEntity('interview');
    interviewComponentsPage = new InterviewComponentsPage();
    await browser.wait(ec.visibilityOf(interviewComponentsPage.title), 5000);
    expect(await interviewComponentsPage.getTitle()).to.eq('bugtrackerjhipsterApp.interview.home.title');
  });

  it('should load create Interview page', async () => {
    await interviewComponentsPage.clickOnCreateButton();
    interviewUpdatePage = new InterviewUpdatePage();
    expect(await interviewUpdatePage.getPageTitle()).to.eq('bugtrackerjhipsterApp.interview.home.createOrEditLabel');
    await interviewUpdatePage.cancel();
  });

  it('should create and save Interviews', async () => {
    const nbButtonsBeforeCreate = await interviewComponentsPage.countDeleteButtons();

    await interviewComponentsPage.clickOnCreateButton();
    await promise.all([
      interviewUpdatePage.setOccuredDateInput('2000-12-31'),
      interviewUpdatePage.setJobTitleInput('jobTitle'),
      interviewUpdatePage.setDescriptionInput('description'),
      interviewUpdatePage.setSalaryInput('5'),
      interviewUpdatePage.setContactInput('contact'),
      interviewUpdatePage.typeSelectLastOption(),
      interviewUpdatePage.opportunitySelectLastOption()
    ]);
    expect(await interviewUpdatePage.getOccuredDateInput()).to.eq('2000-12-31', 'Expected occuredDate value to be equals to 2000-12-31');
    expect(await interviewUpdatePage.getJobTitleInput()).to.eq('jobTitle', 'Expected JobTitle value to be equals to jobTitle');
    expect(await interviewUpdatePage.getDescriptionInput()).to.eq('description', 'Expected Description value to be equals to description');
    expect(await interviewUpdatePage.getSalaryInput()).to.eq('5', 'Expected salary value to be equals to 5');
    expect(await interviewUpdatePage.getContactInput()).to.eq('contact', 'Expected Contact value to be equals to contact');
    await interviewUpdatePage.save();
    expect(await interviewUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await interviewComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1, 'Expected one more entry in the table');
  });

  it('should delete last Interview', async () => {
    const nbButtonsBeforeDelete = await interviewComponentsPage.countDeleteButtons();
    await interviewComponentsPage.clickOnLastDeleteButton();

    interviewDeleteDialog = new InterviewDeleteDialog();
    expect(await interviewDeleteDialog.getDialogTitle()).to.eq('bugtrackerjhipsterApp.interview.delete.question');
    await interviewDeleteDialog.clickOnConfirmButton();

    expect(await interviewComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
