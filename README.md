# hackathon-24-7

### Issues found:

During the hackathon I found the following issues in Control Center

- [ ] https://github.com/vaadin/control-center/issues/855
- [ ] https://github.com/vaadin/control-center/issues/856
- [ ] https://github.com/vaadin/control-center/issues/857
- [ ] https://github.com/vaadin/control-center/issues/858
- [ ] https://github.com/vaadin/control-center/issues/859
- [ ] https://github.com/vaadin/control-center/issues/860
- [ ] https://github.com/vaadin/control-center/issues/861
- [ ] https://github.com/vaadin/control-center/issues/862
- [ ] https://github.com/vaadin/control-center/issues/863
- [ ] https://github.com/vaadin/control-center/issues/864


### What I did

I worked with a modified version of Bakery prepared for control-center (security and localization)

#### Bakery preparation for Control Center

These changes are in this bakery [branch](https://github.com/vaadin/bakery-app-starter-flow-spring/compare/cc-24.7?expand=1):

  - Bump vaadin to 24.7.0.rc1
  - Adapt Bakery to work with CC (Add CC dependency, configure spring and beans)
  - Add internationalization to Bakery
  - Add Docker configuration
  - Push image to docker hub as `k8sdemos/bakery-cc:next`

#### Deploying bakery to Control Center

1. In a cluster I deployed the app by using the following parameters:
  - Image: k8sdemos/bakery-cc:next
  - Startup Delay: 120
  - Generate Certificate
  - Identity Management: enabled

2. Select the application, and Identity Management add apropriate roles/groups/users for bakery
  - role: admin, group: admin, user: admin@vaadin.com
  - role: barista, group: barista, user: barista@vaadin.com
  - role: baker, group: baker, user: baker@vaadin.com

  __Note that roles should match those, group and users might be different__

3. Enable Localization
  - Upload properties file in the repo
  - Change strings
  - Start preview

  __Note that you need to provide appropriate preview IP in your host file__


