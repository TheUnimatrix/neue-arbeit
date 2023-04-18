$(document).ready(() => {
	$("#startYear").change(selectValueChange);
	$("#endYear").change(selectValueChange);
});

function selectValueChange(event) {

	// hole beide Auswahllisten
	let $selectStartYear = $("#startYear");
	let $selectEndYear = $("#endYear");

	// hole die Komponente, welche das Event ausgelöst hat
	let $eventTarget = $(event.target);

	// lies die Jahreswerte aus
	let startYear = parseInt($selectStartYear.val());
	let endYear = parseInt($selectEndYear.val());

	// überprüfe, welches Element das Event ausgelöst hat und ob die Jahreswerte ungültig geworden sind
	if ($selectStartYear.is($eventTarget) && startYear > endYear) {
		$selectEndYear.val(startYear);
	} else if ($selectEndYear.is($eventTarget) && startYear > endYear) {
		$selectStartYear.val(endYear);
	}
}