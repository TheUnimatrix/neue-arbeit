function selectValueChange(selectList, form) {
	selectStartYear = form.elements[form.name + ":startYear"];
	selectEndYear = form.elements[form.name + ":endYear"];
	
	startYear = selectStartYear.value;
	endYear = selectEndYear.value;
	
	if (selectList == selectStartYear && startYear > endYear) {
		selectEndYear.value = startYear;
	} else if (selectList == selectEndYear && startYear > endYear) {
		selectStartYear.value = endYear;
	}
}