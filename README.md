# CRICBEAR

CRICBEAR is a comprehensive cricket management system with separate frontend and backend components.

## Project Structure

```
CRICBEAR/
├── CRICBEAR_Backend/
└── CRICBEAR_Frontend/
    ├── dashboardforadmincoach/
    └── dashboardforpublicuser/
```

## CRICBEAR_Backend

The backend component of CRICBEAR is implemented in Spring Boot. It provides the necessary APIs and server-side logic to support the frontend applications.

For more information about the backend, please refer to the README.md file in the `CRICBEAR_Backend` directory.

## CRICBEAR_Frontend

This repository contains the frontend code for the CRICBEAR application. The project is divided into two main components: the admin/coach dashboard and the public user dashboard.

### Getting Started

1. Clone this repository:

```bash
git clone https://github.com/shreyanshtri26/CRICBEAR.git
cd CRICBEAR_Frontend
```

2. Choose which dashboard you want to run (admin/coach or public user)
3. Follow the setup, running, and testing instructions for the chosen dashboard as detailed below

### Setup, Running, and Testing

#### 1. Dashboard for Admin and Coach

Located in the `dashboardforadmincoach` directory.

1. Navigate to the directory:

```bash
cd dashboardforadmincoach
```

2. Install dependencies:

```bash
npm install
```

3. Start the application:

```bash
npm start
```

4. Run tests:

```bash
npm test
```

##### Functionality

Admin:
* Login and create a tournament
* Manage users and change their Role
* Start the tournament to generate the schedule
* Start a match
* Initiate the semi-final and final

Coach:
* Register to the application
* Login and create the team
* Edit and update the team
* Register to a tournament

#### 2. Dashboard for Public User

Located in the `dashboardforpublicuser` directory.

1. Navigate to the directory:

```bash
cd dashboardforpublicuser
```

2. Install dependencies:

```bash
npm install
```

3. Start the application:

```bash
npm start
```

4. Run tests:

```bash
npm test
```

##### Functionality

User:
* View the public dashboard

### Running Both Dashboards Simultaneously

To run both dashboards at the same time, you'll need to open two terminal windows or tabs:

1. In the first terminal:

```bash
cd CRICBEAR_Frontend/dashboardforadmincoach
npm install
npm start
```

2. In the second terminal:

```bash
cd CRICBEAR_Frontend/dashboardforpublicuser
npm install
npm start
```

Note: You may need to configure different ports for each application if they both try to use the same default port.

### Testing Both Dashboards

To run tests for both dashboards, use separate terminal windows or tabs:

1. For the admin/coach dashboard:

```bash
cd CRICBEAR_Frontend/dashboardforadmincoach
npm test
```

2. For the public user dashboard:

```bash
cd CRICBEAR_Frontend/dashboardforpublicuser
npm test
```
