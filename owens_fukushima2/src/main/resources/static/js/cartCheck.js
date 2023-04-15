/**
 *
 */

function Test() {
 var listSize = document.getElementById("listSize").value;
	 if(listSize != 0){
		 var checked = confirm('カートに商品が入っております。カートに商品を入れたままログアウトしますか？');

		 if (checked == true) {
	         return true;
	     } else {
	         return false;
	     }
	 }

}