CREATE TABLE category_departments (
    category_id SMALLINT NOT NULL,
    department_id SMALLINT NOT NULL,
    PRIMARY KEY (category_id, department_id),
    FOREIGN KEY (category_id) REFERENCES categories(id) ON DELETE CASCADE,
    FOREIGN KEY (department_id) REFERENCES departments(id) ON DELETE CASCADE
);