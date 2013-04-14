﻿$(function(){
	showCurrentPageNo();
	initBtnPager();
	
	
});
function getObjectKeys(object)
{
    var keys = [];
    for (var property in object)
    {
      keys.push(property);
    }
    return keys;
}
/**
function post(URL, PARAMS) {
    var temp = document.createElement("form");      
    temp.action = URL;      
    temp.method = "post";      
    temp.style.display = "none";      
    for (var x in PARAMS) {
        var opt = document.createElement("textarea");      
        opt.name = x;
        opt.value = PARAMS[x];      
        temp.appendChild(opt);      
    }
    document.body.appendChild(temp);      
    temp.submit();
    return temp;
}
**/
function showCurrentPageNo(){
	var hiddenPageNo = parseInt($("#hiddenPageNo").html());
	var totalPageNo = parseInt($("#spanTotalPageNo").html());
	var currentPageNo = hiddenPageNo>totalPageNo?totalPageNo:hiddenPageNo;
	currentPageNo = hiddenPageNo<1?1:currentPageNo;
	$("#spanCurrentPageNo").html(""+currentPageNo);
}
function initBtnSearchByIp(firstPageAction){
	$("#btnSearchByIp").click(function(){
		var isIp = /^[\d|\.]+$/.test($("#txtMachineIp").val());
		if(!isIp){
			Dialog.alert("请输入正确的ip地址");
			return;
		}
		var json = {"like":{"machine_ip":$("#txtMachineIp").val()}};
		//alert(JSON.stringify(json));
		var url = firstPageAction+ "?xuechongyang!filter="+JSON.stringify(json)+"&max_per_page="+$("#ddlMaxPerPage").val();
		window.location = encodeURI(url,'UTF-8');
	});
}

function initPagerBtnAndChangeFilter(firstPageAction,prevPageAction,nextPageAction,lastPageAction){
	var hidden_filter_state = {}
	if($("#hidden_filter").html()!=""){
		eval("hidden_filter_state="+$("#hidden_filter").html());
	}
	var prefix = "filter_state_";
	var attrToIdentify = "name";
	var triggerAttr = "trigger";
	var trigger_json = {};
	//var changed_state = hidden_filter_state;

	
	function changeHiddenFilter($this){
			
			if($("#hidden_filter").html()!=""){
				eval("var hidden_filter_state="+$("#hidden_filter").html());
			}else{
				var hidden_filter_state = {};
			}
			var currentValue = $this.val();
			var currentKey =$this.attr(attrToIdentify).substr(prefix.length);
			var currentStat = $this.attr("stat");
			if(currentValue=="" || currentValue=="-1"){
			if(hidden_filter_state.hasOwnProperty(currentStat) && hidden_filter_state[currentStat].hasOwnProperty(currentKey)){
				//hidden_filter_state[key][currentKey] = currentValue;
				delete hidden_filter_state[currentStat][currentKey];//currentStat是类似于equal和like这样的string
				if(getObjectKeys(hidden_filter_state[currentStat]).length <= 0){//因为是一个双层的json,里层删光了,把外层删了,这样就连like,equal也不带了
					delete hidden_filter_state[currentStat];//这样就不会造成url里filter={"equal":{}}的bug了
				}
			}
				return hidden_filter_state;  //为何要加return我研究下..这个话还有点bug,hidden_filter_state是类似于{"equal":{"DATE_FORMAT(start_datetime,\"%Y%m%d\")":20120515,"run_mode":"0","exec_type":"2","is_process_node":"0"},"like":{...}}这样的字符串
			}
			if(!hidden_filter_state.hasOwnProperty(currentStat)){
				hidden_filter_state[currentStat] = {}
			}
			hidden_filter_state[currentStat][currentKey] = currentValue;
			$("#hidden_filter").html(JSON.stringify(hidden_filter_state));
			//alert("change:"+$("#hidden_filter").html());
			//alert(hidden_filter_state.length+" go gogo");
			return hidden_filter_state;
	}
	
	
	$("["+attrToIdentify+"^='"+prefix+"']").each(function(){
		var stat = $(this).attr("stat");
		if(hidden_filter_state.hasOwnProperty(stat)){
			for(var key in hidden_filter_state[stat]){
				var current_attr_value =  $(this).attr(attrToIdentify);
				if(key ==current_attr_value.substr(prefix.length)){
					var new_state = hidden_filter_state[stat][key];
					$(this).val(new_state);
					break;		
				}
			}
		}
			
			var $this =$(this);
			function validationAndRedirect(){
				
				var valid_value =$this.val();
				var redirect = true;
				if(undefined !=$this.attr("valid")){
				var invalid_msg = $this.attr("invalid_msg");
				eval("var valid_ret = "+$this.attr("valid")+"('"+valid_value+"')");
				if(!valid_ret){
					redirect = false;
					Dialog.alert(invalid_msg);
					return;
				}
				}
				if(redirect){
				var hidden_filter_state = changeHiddenFilter($this);
				var location = firstPageAction+"?max_per_page="+$("#ddlMaxPerPage").val();
				var filter_length = getObjectKeys(hidden_filter_state).length;
				if(filter_length>0){
					location+= "&filter="+JSON.stringify(hidden_filter_state);
				}
				if($("#hidden_tableName").html()!=""){
					location += "&tableName="+$("#hidden_tableName").html();
				}
				//alert(location);
				window.location = encodeURI(location,'UTF-8');
				}
			}
	
			
			
			if(undefined != $(this).attr("trigger_target")){
			if("this" == $(this).attr("trigger_target")){
			$(this).bind($(this).attr(triggerAttr),function(){
					validationAndRedirect();
				});
			}else{
			$("#"+$(this).attr("trigger_target")).bind($(this).attr(triggerAttr),function(){
					validationAndRedirect();
			});
			}
			}else{
				$(this).bind($(this).attr(triggerAttr),function(){
					validationAndRedirect();
				});
			}
			
			/**

			alert(JSON.stringify(hidden_filter_state));
			**/
			
		
	});
	initBtnPager(firstPageAction,prevPageAction,nextPageAction,lastPageAction);
}

function initBtnPager(firstPageAction,prevPageAction,nextPageAction,lastPageAction){
	$("#ddlMaxPerPage").val($("#hiddenMaxPerPage").html());
	var filterParam = "";
	if($("#hidden_filter").html()!=""){
		eval("var judge_hidden_json="+$("#hidden_filter").html());
		if(getObjectKeys(judge_hidden_json).length>0){
		filterParam = "&filter="+encodeURIComponent($("#hidden_filter").html(),'UTF-8');
		}
	}
	if($("#hidden_tableName").html()!=""){
		filterParam += "&tableName="+encodeURIComponent($("#hidden_tableName").html(),'UTF-8');
	}
	//if (null!=filter && ""!=filter){
	//	filterParam = "&filter="+filter;
	//}
	$("#btnFirstPage").attr("href",firstPageAction+"?max_per_page="+$('#ddlMaxPerPage').val()+filterParam);
	$("#btnPrevPage").attr("href",prevPageAction+"?current_page_no="+$('#spanCurrentPageNo').html()+"&max_per_page="+$('#ddlMaxPerPage').val()+filterParam);
	$("#btnNextPage").attr("href",nextPageAction+"?current_page_no="+$('#spanCurrentPageNo').html()+"&max_per_page="+$('#ddlMaxPerPage').val()+filterParam);
	$("#btnLastPage").attr("href",lastPageAction+"?max_per_page="+$('#ddlMaxPerPage').val()+filterParam);
	var currentPageNo =parseInt($('#spanCurrentPageNo').html());
	var totalPageCount = parseInt($('#spanTotalPageNo').html());
	if(currentPageNo>=totalPageCount){
		$("#btnNextPage").removeAttr("href").unbind().click(function(){Dialog.alert("已经到最后一页");});
		$("#btnLastPage").removeAttr("href").unbind().click(function(){Dialog.alert("已经到最后一页");});
	}
	if(currentPageNo<=1){
		$("#btnFirstPage").removeAttr("href").unbind().click(function(){Dialog.alert("已经到第一页");});
		$("#btnPrevPage").removeAttr("href").unbind().click(function(){Dialog.alert("已经到第一页");});
	}
	
	$("#ddlMaxPerPage").change(function(){
		var location = firstPageAction+"?max_per_page="+$(this).val();
		if($("#hidden_filter").html()!=""){
		eval("var judge_hidden_json="+$("#hidden_filter").html());
		if(getObjectKeys(judge_hidden_json).length>0){
			location += "&filter="+$("#hidden_filter").html();
		}
		}
		if($("#hidden_tableName").html()!=""){
		location += "&tableName="+$("#hidden_tableName").html();
		}
		window.location = encodeURI(location,'UTF-8');
	});
}