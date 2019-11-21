new Vue({
    el:"#app",
    data:{
        brandList:[],
        brand:{
            name:'',
            firstChar:''
        },
        page:1,//显示当前是哪一页
        pageSize:10,//每页有多少条记录
        total:100,//总记录数
        maxPage:9,//共多少页
        selectedIds:[],
        searchBrand:{//搜索品牌实体
            name:'',
            firstChar:''
        },
    },
    methods:{
        pageHandler:function (page) {
            var _this = this;
            this.page = page;
            //查询用户列表
            axios.post("/brand/findPage.do?page="+page+"&pageSize=10",this.searchBrand).then(function (response) {
                _this.brandList = response.data.rows;
                _this.total = response.data.total;
                console.log(response);
            }).catch(function (reason) {
                console.log(reason);
            })
        },
        save:function () {
            var _this = this;
            var url = '';
            //区分是添加还是修改
             if (_this.brand.id!=null){
                 url = "/brand/update.do"   //更新操作
             }else {
                 url = "/brand/add.do"      //添加操作
             }
            axios.post(url,this.brand).then(function (value) {
                if (value.data.success){
                    alert(value.data.message);
                    _this.pageHandler(1);
                    _this.brand={};
                    console.log(value)
                }
            }).catch(function (reason) {
                console.log(reason)
            })
        },
        deleteSelection:function(event,id){
           //复选框选中
            if (event.target.checked){
                //选中
                this.selectedIds.push(id)
            }else {
                //取消选中
                //移除选中元素
                var idx = this.selectedIds.indexOf(id);
                this.selectedIds.splice(idx);
            }
        },
        deleteBrand:function () {
            var _this = this;
            //使用qs插件 处理数组
            axios.post('/brand/delete.do',Qs.stringify({ids: _this.selectedIds},{ indices: false }))
                .then(function (response) {
                    if (response.data.success){
                        alert(response.data.message)
                    }else {
                        alert(response.data.message)
                    }
                    _this.pageHandler(1);
                    _this.selectedIds = [];
                }).catch(function (reason) {
                alert(reason.message);
            })
        },


        findById:function (id) {
           var _this = this;
            axios.get("/brand/findById.do",{params:{id:id}}).then(function (value) {
                //获取服务端响应的结果
                _this.brand = value.data;
                console.log(value);
            }).catch(function (reason) {
                console.log(reason);
            })
        }

    },
    created: function() {
        this.pageHandler(1);
    }
});