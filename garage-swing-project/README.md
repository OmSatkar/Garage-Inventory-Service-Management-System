# Garage Inventory & Management System (Java Swing + PostgreSQL)

This is the upgraded desktop build of your project with a modern Swing UI inspired by the soft glass/gradient design reference you shared.

## Tech Stack
- Java 17+
- Java Swing frontend
- JDBC backend connectivity
- PostgreSQL database
- Maven project structure for Eclipse import

## Main Features
- Admin and Staff login
- Role-based dashboard and navigation sidebar
- Modern Swing UI with gradient panels, rounded inputs, colored action buttons, styled tables, and dashboard cards
- Inventory CRUD with search and category filter
- Low stock rows highlighted automatically
- Supplier CRUD
- Service record CRUD
- Billing generation with invoice preview and printable bill
- Staff management
- Task assignment for staff and separate My Tasks view
- Dashboard cards + chart + low stock alert table

## UI Preview
A preview image is included here:
- `docs/ui-preview.png`

## Project Structure
```text
garage-swing-project/
├── pom.xml
├── README.md
├── docs/
│   └── ui-preview.png
├── sql/
│   └── garageims_postgresql.sql
└── src/main/java/com/garageims/
    ├── config/
    ├── dao/
    ├── model/
    ├── ui/
    │   ├── components/
    │   ├── dialog/
    │   ├── GarageApp.java
    │   ├── LoginFrame.java
    │   └── MainFrame.java
    └── util/
        └── UIUtils.java
```

## Setup in Eclipse
1. Install Java 17 or above.
2. Install PostgreSQL.
3. Create a database named `garageims`.
4. Open PostgreSQL query tool and run `sql/garageims_postgresql.sql`.
5. Open Eclipse.
6. Go to **File > Import > Maven > Existing Maven Projects**.
7. Select the extracted project folder.
8. Click **Finish**.
9. Right click the project and choose **Maven > Update Project**.
10. Open `src/main/java/com/garageims/config/AppConfig.java`.
11. Update the DB URL, username, and password if needed.
12. Run `GarageApp.java` as **Java Application**.

## Default Login
- Admin: `admin / admin123`
- Staff: `staff1 / staff123`

## Important Notes
- This is a desktop Swing app, not a servlet/Tomcat web app.
- Passwords are stored in plain text only for academic simplicity.
- PostgreSQL JDBC will be resolved through Maven.
- The UI layer was fully redesigned to look much more polished while keeping the project beginner-friendly.

## Modules Included
### 1. Login
- Username/password authentication
- Role-based entry to admin or staff workspace

### 2. Dashboard
- Total items
- Low stock count
- Today's services
- Total staff
- Today's revenue
- Chart panel
- Low stock alert table

### 3. Inventory
- Add, update, delete inventory items
- Search by text
- Filter by category
- Low stock status

### 4. Suppliers
- Add supplier
- Edit supplier
- Delete supplier
- Link supplier to inventory item through inventory form

### 5. Services
- Add vehicle service record
- Edit service record
- Delete service record
- View service history

### 6. Billing
- Choose service
- Auto-fill bill amount
- Generate bill
- Preview invoice
- Print invoice

### 7. Staff
- Add/edit/delete staff members (admin only)

### 8. Tasks
- Assign tasks to staff (admin only)
- Staff can view their own assigned tasks

## Suggested Future Improvements
- BCrypt password hashing
- PDF invoice export
- Date-range reports
- Inventory consumption linked to service parts
- Charts with a real charting library
- Backup/restore support
