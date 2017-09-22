# Stock Market Simulator
Project: Budding Share Market Investor

**Team Members:**
- Jayden Joyce: s3543824
- Karsten Luu: s3542764
- Chew Yiquan: s3522101
- John Nguyen: s3542969

**Building the Program with eclipse**
- Enter "clean install" in Goals in the Run configuration.

**Running the program in eclipse**
1. Ensure that the maven plugin is already install in your eclipse. If not please check http://www.eclipse.org/m2e/
2. After maven build, select Main in src/main/java and run as Java Application.
3. Open your browser, type in and go to "localhost:4567".

**Deploying to cloud (Heroku)**
1. Create an account in Heroku
2. Download and install Heroku CLI: https://devcenter.heroku.com/articles/heroku-cli#windows
3. Go to project root, then enter: mvn heroku:deploy
Reference: http://sparkjava.com/tutorials/heroku