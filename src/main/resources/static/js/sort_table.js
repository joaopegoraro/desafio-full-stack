(function () {
  /**
   * Sorts a HTML table.
   *
   * @param {HTMLTableElement} table The table to sort
   * @param {number} column The index of the column to sort
   * @param {boolean} asc Determines if the sorting will be in ascending
   */
  function sortTableByColumn(table, column, asc = true) {
    const dirModifier = asc ? 1 : -1;
    const tBody = table.tBodies[0];
    const rows = Array.from(tBody.querySelectorAll(".person-table__row"));

    // Sort each row
    const sortedRows = rows.sort((a, b) => {
      console.log(column);
      const aColText = a
        .querySelector(
          `.person-table__cell:not(.person-table__cell--no-bg):nth-child(${
            column + 1
          })`
        )
        .textContent.trim();
      const bColText = b
        .querySelector(
          `.person-table__cell:not(.person-table__cell--no-bg):nth-child(${
            column + 1
          })`
        )
        .textContent.trim();

      return aColText > bColText ? 1 * dirModifier : -1 * dirModifier;
    });

    // Remove all existing TRs from the table
    while (tBody.firstChild) {
      tBody.removeChild(tBody.firstChild);
    }

    // Re-add the newly sorted rows
    tBody.append(...sortedRows);

    // Remember how the column is currently sorted
    table.querySelectorAll("th").forEach((th) => th.removeAttribute("orderBy"));
    table
      .querySelector(`th:nth-child(${column + 1})`)
      .setAttribute("orderBy", asc ? "asc" : "desc");
  }

  document.querySelectorAll(".person-table").forEach((tableElement) => {
    const tableHead = tableElement.querySelector(".person-table__head");
    tableElement
      .querySelectorAll(".person-table__header")
      .forEach((headerCell, index) => {
        console.log(headerCell);
        headerCell.addEventListener("click", () => {
          const currentIsAscending =
            headerCell.getAttribute("orderBy") == "asc";

          sortTableByColumn(tableElement, index, !currentIsAscending);
        });
      });
  });
})();
