function SnPage(frm, action, totalPage, currentPage) {
	this.currentPage = currentPage;
	this.totalPage = totalPage;
	this.form = frm;
	this.action = action;
	this.goToPage = function(number, isValidate) {
		var reg1 = /^\d+$/;
		if (number.match(reg1) == null) {
			alert("请检查页码格式");
			return;
		}
		var c = parseInt(number);
		var t = parseInt(totalPage);
		if (!c || c < 1) {
			alert('跳转页数不正确!');
			return;
		} else if (c > t) {
			alert('跳转页数不能大于总页数:' + t);
			return;
		}
		this.form.elements["currentPage"].value = number;
		this.form.action = this.action;
		this.form.submit();
	}
}
