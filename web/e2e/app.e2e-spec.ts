import { VersionTestPage } from './app.po';

describe('version-test App', () => {
  let page: VersionTestPage;

  beforeEach(() => {
    page = new VersionTestPage();
  });

  it('should display welcome message', done => {
    page.navigateTo();
    page.getParagraphText()
      .then(msg => expect(msg).toEqual('Welcome to app!!'))
      .then(done, done.fail);
  });
});
