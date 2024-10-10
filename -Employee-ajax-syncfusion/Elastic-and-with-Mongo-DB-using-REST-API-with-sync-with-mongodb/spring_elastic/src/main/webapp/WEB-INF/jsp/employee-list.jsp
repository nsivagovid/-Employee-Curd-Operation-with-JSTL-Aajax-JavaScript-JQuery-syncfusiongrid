<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html> <!-- Prevent Quirks Mode -->
<html>
<head>
    <title>Employee List</title>

    <!-- Bootstrap CSS -->
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">

    <!-- Syncfusion CSS from CDN -->
    <link href="https://cdn.syncfusion.com/ej2/19.4.38/material.css" rel="stylesheet" />

    <!-- Syncfusion JavaScript from CDN -->
    <script src="/js/ej2.min.js"></script>

    <!-- Register Syncfusion License Key from the Controller -->
    <script>
        if (typeof ej !== 'undefined' && ej.base && ej.base.License) {
            ej.base.License.register("${syncfusionLicenseKey}");
        } else {
            console.error("Syncfusion library not loaded");
        }
    </script>

    <style>
        .e-toolbar .e-toolbar-items .e-tbar-btn {
            padding: 5px 10px;
            font-size: 14px;
            border-radius: 4px;
        }

        .e-toolbar .e-toolbar-items .e-icons {
            font-size: 16px;
        }

        .e-toolbar .e-toolbar-items .e-toolbar-item {
            margin-right: 10px;
        }
    </style>
</head>
<body>
<div class="container">
    <h2 class="mt-4">Employee List with Syncfusion Grid</h2>

    <a href="/employees/add" class="btn btn-success mb-3">Add New Employee</a>

    <!-- Syncfusion Data Grid element -->
    <div id="Grid"></div>

    <script>
        // Load employee data using JSTL
        var data = [
            <c:forEach var="employee" items="${employees}">
            { id: "${employee.id}", name: "${employee.name}", department: "${employee.department}", salary: "${employee.salary}" },
            </c:forEach>
        ];

        // Initialize Syncfusion Grid
        var grid = new ej.grids.Grid({
            dataSource: data,
            toolbar: ['Add', 'Edit', 'Delete', 'Update', 'Cancel'],
            allowPaging: true,
            allowSorting: true,
            editSettings: { allowEditing: true, allowAdding: true, allowDeleting: true, mode: 'Normal' },
            columns: [
                { field: 'id', headerText: 'ID', isPrimaryKey: true, width: 120, textAlign: 'Right' },
                { field: 'name', headerText: 'Name', width: 150, validationRules: { required: true } },
                { field: 'department', headerText: 'Department', width: 150 },
                { field: 'salary', headerText: 'Salary', width: 150 }
            ],
            actionComplete: function (e) {
                if (e.requestType === 'save') {
                    // Save updated or newly added record via AJAX
                    $.ajax({
                        type: 'POST',
                        url: '/employees/save',
                        contentType: 'application/json',
                        data: JSON.stringify(e.data),
                        success: function (response) {
                            console.log("Employee saved successfully");
                            grid.refresh();  // Refresh the grid after successful operation
                        },
                        error: function () {
                            console.error("Failed to save employee");
                        }
                    });
                } else if (e.requestType === 'delete') {
                    // Delete record via AJAX
                    $.ajax({
                        type: 'DELETE',
                        url: '/employees/delete/' + e.data[0].id,
                        success: function (response) {
                            console.log("Employee deleted successfully");
                            grid.refresh();  // Refresh the grid after deletion
                        },
                        error: function () {
                            console.error("Failed to delete employee");
                        }
                    });
                }
            }
        });

        grid.appendTo('#Grid');
    </script>
</div>

<!-- Bootstrap JS and dependencies -->
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.16.0/umd/popper.min.js"></script>
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>
</body>
</html>
