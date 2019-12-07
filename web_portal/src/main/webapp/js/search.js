
new Vue({
    el:"#app",
    data:{
        searchMap:{
            'keywords':'',//搜索关键字
            'pageNo':1,//当前页
            'pageSize':10,//每页展示多少条数据
        },
        resultMap:{
            rows:[],
            total:0, //总记录数
            totalPages:0 //总页数
        },
        pageLabel:[],//所有页码
        firstDot:true,//前面有点
        lastDot:true,//后边有点
    },
    methods:{
        getQueryString:function (name) {
            var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)","i");
            var r = window.location.search.substr(1).match(reg);
            if (r!=null){
                return (decodeURI(r[2]));
            }
            return null;
        },
        search:function () {
            this.searchMap.pageNo= parseInt(this.searchMap.pageNo);//转换为数字
            var _this = this;
            axios.post("/itemsearch/search.do",this.searchMap)
                .then(function (response) {
                    _this.resultMap = response.data;
                    _this.buildPageLabel(_this);
                }).catch(function (reason) {
                console.log(reason.data);
            });
        },
        buildPageLabel:function (obj) {
            obj.pageLabel = [];
            var firstPage = 1; //开始页码
            var lastPage = obj.resultMap.totalPages; //戒指页码
            obj.firstDot = true;//前面有点
            obj.lastDot = true;//后面有点
            if (obj.resultMap.totalPages>5){//如果页码数量大于5
                if (obj.searchMap.pageNo<3){//如果当前页小于等于3，显示前五页
                    lastPage = 5;
                    obj.firstDot = false;//前面没点
                }else if (obj.searchMap.pageNo>obj.resultMap.totalPages-2){//显示后5页
                    firstPage = obj.resultMap.totalPages - 4;
                    obj.lastDot = false;//后面没点
                }else {//显示以当前页为中心的5页
                     firstPage = obj.searchMap.pageNo - 2;
                     lastPage = obj.searchMap.pageNo +2;
                }

            }else {
                 obj.firstDot = false//前面无点
                obj.lastDot = false //后面无点
            }
            for (var i = firstPage;i<=lastPage;i++){
                obj.pageLabel.push(i);
            }

        },
        //分页查询
        queryByPage:function (page) {
            if (page<1||page>this.resultMap.totalPages){
                return;
            }
            this.searchMap.pageNo = page;
            this.search();
        },
        //判断是否是第一页
        isTopPage:function () {
            if (this.searchMap.pageNo==1){
                return true;
            }else {
                return false;
            }
        },
        //判断当前是否是最后一页
        isEndPage:function () {
            if (this.searchMap.pageNo==this.resultMap.totalPages){
                return true;
            }else {
                return false;
            }
        }

    },
    watch: { //监听属性的变化

    },
    created: function() {//创建对象时调用

    },
    mounted:function () {//页面加载完
        var sc = this.getQueryString("sc");
        this.searchMap.keywords = sc;
        this.search();
    }
});