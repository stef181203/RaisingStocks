/* Variables that we use for the initial load
* as well as for manipulating the data based on user input. */
let currentPage = 0;
const pageSize = 15;
let sort = "code-asc"
$(document).ready(function() {
    /* This function is used to send an API GET request to CompanyController with page, size and sort parameters
    * used for Pagination of the companies in the table. It expects a response with a list of CompanyDTO objects
    * and a total page count inside the response body in order to enable/disable the pagination buttons and create
    * table rows with the company information on the client side. */
    function fetchTableData() {
        fetch(`/api/companies?page=${currentPage}&pageSize=${pageSize}&sort=${sort}`)
            .then(response => response.json())
            .then(data => {
                let companies = data.companies;
                let totalPageCount = data.totalPageCount;
                const tableBody = document.querySelector("#companiesTable tbody")
                tableBody.innerHTML = "";
                companies.forEach(company => {
                    const row = document.createElement("tr");
                    row.innerHTML = `
                        <th scope="row">${company.companyId}</th>
                        <td>${company.code}</td>
                        <td>${company.name}</td>
                        <td>${company.latestTurnoverDate}</td>
                    `;
                    tableBody.appendChild(row);
                });

                // Here we disable the pagination buttons based on the current page or the total pages available
                $("#prevPage").prop('disabled', currentPage === 0);
                $("#nextPage").prop('disabled', currentPage === totalPageCount - 1);
            })
            .catch(error => {
                console.error("Error fetching the API: ", error);
            });
    }

    // Initial data fetch during page loading
    fetchTableData();

    /* This adds functionality to the dropdown where we can choose
    * how to sort the companies in the table */
    $("#sortFilterDropdown").change(function () {
        sort = $(this).val();
        currentPage = 0;
        fetchTableData();
    });

    // This adds functionality to the button that decreases the current page number
    $("#prevPage").click(function() {
        if(currentPage > 0) {
            currentPage--;
            fetchTableData();
        }
    });

    // This adds functionality to the button that increases the current page number
    $("#nextPage").click(function() {
        currentPage++;
        fetchTableData();
    });

});