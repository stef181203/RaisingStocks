/* Variables that we use for the initial load
* as well as for manipulating the data based on user input. */
let companyId = 1;
let currentPage = 0;
const pageSize = 15;
let sort = "date-closest";
let graphYear;
$(document).ready(function() {
    /* This function is sending an API GET request to StocksController and in the response body
    * expects a list of the requested stocks, total page count used for enabling/disabling the pagination buttons and
    * the code of the company whose stocks we are showing in the stocks table. In the request there are the current page number,
    * the size of the page and the sort method. We also send the company id so the backend knows which stocks to get from the database. */
    function fetchStocksTableData() {
        fetch(`/api/stocks?companyId=${companyId}&page=${currentPage}&pageSize=${pageSize}&sort=${sort}`)
            .then(response => response.json())
            .then(data => {
                let stocks = data.stocks;
                let totalPageCount = data.totalPageCount;
                let companyCode = data.companyCode;
                const tableBody = document.querySelector("#stocksTable tbody")
                tableBody.innerHTML = "";
                stocks.forEach(stock => {
                    const row = document.createElement("tr");
                    row.innerHTML = `
                        <td>${stock.date}</td>
                        <td>${stock.lastTransactionPrice}</td>
                        <td>${stock.maxPrice}</td>
                        <td>${stock.minPrice}</td>
                        <td>${stock.averagePrice}</td>
                        <td>${stock.averagePercentage}%</td>
                        <td>${stock.quantity}</td>
                        <td>${stock.turnoverInBestDenars}</td>
                        <td>${stock.totalTurnoverInDenars}</td>
                    `;
                    tableBody.appendChild(row);

                    /* This code is updating the selected option of the dropdown
                    list where we choose the company and show their stocks. */
                    let dropdown = document.querySelector("#companiesDropdown");
                    let option = dropdown.querySelector(`option[value="${companyId}"]`);
                    option.selected = true;

                    // We use the company code from the response body here to update the text above the table
                    document.getElementById("stocksText").innerHTML = `Stocks Table for ${companyCode}`;
                });

                // Disabling the buttons based on the current page or the total pages available
                $("#prevPage").prop('disabled', currentPage === 0);
                $("#nextPage").prop('disabled', currentPage === totalPageCount - 1);
            })
            .catch(error => {
                console.error("Error fetching the API: ", error);
            });
    }

    /* This function gets the company ids, codes and names in the body of the response
    * from StocksController and then fills the dropdown list with the company information.
    * We then use that dropdown to switch to stocks of a different company. */
    function fetchCompaniesDropdownData() {
        fetch("/api/companies/all")
            .then(response => response.json())
            .then(data => {
                const dropdownList = document.getElementById("companiesDropdown");
                data.forEach(company => {
                    dropdownList.innerHTML += `
                    <option value="${company.companyId}">${company.name} - ${company.code}</option>
                    `
                });
            });
    }

    /* This function fetches every available year for the company stocks. For example:
    * if the company doesn't have stock objects recorded in the database for the year 2023,
    * then that year won't be available in this dropdown list. We use the dropdown to switch
    * between years since we are showing the graph information for a timeline of 1 year. */
    function fetchGraphYearsAvailable() {
        return fetch(`/api/stocks/graph-years?companyId=${companyId}`)
            .then(response => response.json())
            .then(data => {
                const dropdownList = document.getElementById("yearDropdown");
                dropdownList.innerHTML = "";
                data.forEach(year => {
                    dropdownList.innerHTML += `
                  <option value="${year}">${year}</option>
                  `
                });

                graphYear = dropdownList.value;
            });
    }

    /* This function writes creates table rows for the given table body
    * and fills them with information from the data object in order
    * to cut down the duplicate code in the two functions below this one. */
    function writeTechnicalAnalysisTableRows(data, tableBody) {
        tableBody.innerHTML = "";
        data.forEach(ind => {
            const row = document.createElement("tr");
            row.innerHTML = `
                        <td>${ind.name} (${ind.code})</td>
                        <td>${ind.dayDataEnough ? ind.valueByDay : 'Not Available'}</td>
                        <td>${ind.weekDataEnough ? ind.valueByWeek : 'Not Available'}</td>
                        <td>${ind.monthDataEnough ? ind.valueByMonth : 'Not Available'}</td>
                    `;
            tableBody.appendChild(row);
        });
    }

    /* This function sends an API GET request with company id parameter to StocksController
    *  and in the response body expects the trend indicators for the stocks based on the company id.
    * The function then proceeds to update the table on the front-end with the information from the response.*/
    function fetchTrendIndicatorsData() {
        fetch(`/api/stocks/trend-indicators?companyId=${companyId}`)
            .then(response => response.json())
            .then(data => {
                const tableBody = document.querySelector("#trendIndicatorsTable tbody");
                writeTechnicalAnalysisTableRows(data, tableBody);
            });
    }

    /* This function sends an API GET request with company id parameter to StocksController
    *  and in the response body expects the momentum indicators for the stocks based on the company id.
    * The function then proceeds to update the table on the front-end with the information from the response.*/
    function fetchMomentumIndicatorsData() {
        fetch(`/api/stocks/momentum-indicators?companyId=${companyId}`)
            .then(response => response.json())
            .then(data => {
                const tableBody = document.querySelector("#momentumIndicatorsTable tbody");
                writeTechnicalAnalysisTableRows(data, tableBody);
            });
    }

    /* This function sends an API GET request with company id parameter to StocksController
    *  and in the response body expects the signals (Buy, Hold, Sell) for the stocks based on the company id.
    * The function then proceeds to update the signals table on the front-end with the information from the response.*/
    function fetchSignals() {
        fetch(`/api/stocks/signals?companyId=${companyId}`)
            .then(response => response.json())
            .then(data => {
                const tableBody = document.querySelector("#signalsTable tbody");
                tableBody.innerHTML = "";
                const row = document.createElement("tr");
                row.innerHTML = `
                        <th>Signals</th>
                        <td>${data[0]}</td>
                        <td>${data[1]}</td>
                    `;
                tableBody.appendChild(row);
            });
    }

    /* This function gets information about a stock object for the current company and the date
    * when this stock object is recorded. The request parameters are the company id and the year
    * for which we request stocks. We are displaying the values on the X-axis and Y-axis and using a basic
    * Chart.js chart without any complex changes to the template code for this JS library. */
    function loadChart() {
        fetch(`/api/stocks/graph?companyId=${companyId}&year=${graphYear}`)
            .then(response => response.json())
            .then(data => {
                const xValues = [];
                const yValues = [];
                data.forEach(dayInfo => {
                    xValues.push(dayInfo.date);
                    yValues.push(dayInfo.price);
                });

                canvasCtx.clearRect(0, 0, canvas.width, canvas.height);
                chartObj !== null && chartObj.destroy();
                chartObj = new Chart("chart", {
                    type: "line",
                    data: {
                        labels: xValues,
                        datasets: [{
                            fill: false,
                            lineTension: 0,
                            borderColor: 'rgb(75, 192, 192)',
                            backgroundColor: '#466cd9',
                            data: yValues
                        }]
                    },
                    options: {
                        legend: {display: false},
                        title: {
                            display: false
                        },
                        elements: {
                            point: {
                                radius: 2,
                                hitRadius: 3
                            }
                        }
                    }
                });
            });
    }

    /* This here is the initial call to these functions that is made
    instantly after the document is ready (during the page load). */
    fetchCompaniesDropdownData();
    fetchStocksTableData();
    fetchTrendIndicatorsData();
    fetchMomentumIndicatorsData();
    fetchSignals();

    // Initially we create an empty canvas in order to paint it with the stocks chart.
    const canvas = document.getElementById("chart");
    const canvasCtx = canvas.getContext('2d');
    let chartObj = null;

    /* Here we are first filling the dropdown list with the available years
    * and then we are loading the chart since we need the last year in the dropdown
    * to show any valid information. */
    fetchGraphYearsAvailable().then(() => {
        loadChart();
    });

    // Giving functionality to the companies dropdown list.
    $("#companiesDropdown").change(function() {
        sort = "date-closest";
        currentPage = 0;
        companyId = $(this).val();
        fetchStocksTableData();
        fetchGraphYearsAvailable().then(() => {
            loadChart();
        });
        fetchTrendIndicatorsData();
        fetchMomentumIndicatorsData();
        fetchSignals();
    });

    // Giving functionality to the sorting dropdown list.
    $("#sortFilterDropdown").change(function () {
        sort = $(this).val();
        currentPage = 0;
        fetchStocksTableData();
    });

    // Giving functionality to the stock year dropdown list.
    $("#yearDropdown").change(function () {
        graphYear = $(this).val();
        loadChart();
    });

    // The next two are event listeners for the pagination buttons.
    $("#prevPage").click(function() {
        if(currentPage > 0) {
            currentPage--;
            fetchStocksTableData();
        }
    });

    $("#nextPage").click(function() {
        currentPage++;
        fetchStocksTableData();
    });

});