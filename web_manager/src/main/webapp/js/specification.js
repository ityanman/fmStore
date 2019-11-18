new Vue({
    el:"#app",
    data:{
        specList:[],
        searchSpec:{//搜索规格实体
            specName:''
        },
        page:1,//显示当前是哪一页
        pageSize:10,//每页有多少条记录
        total:100,//总记录数
        maxPage:9,//共多少页
        selectedIds:[],
        secEntity:{
            specOptionList:[],
            specification:{}
        },
    },
    methods:{
        pageHandler:function (page) {
            var _this = this;
            this.page = page;
            //查询用户列表
            axios.post("/spec/findPage.do?page="+page+"&pageSize=5",this.searchSpec).then(function (response) {
                _this.specList = response.data.rows;
                _this.total = response.data.total;
                console.log(response);
            }).catch(function (reason) {
                console.log(reason);
            })
          },
        addRow:function () {
            this.secEntity.specOptionList.push({});
        },
        deleteTableRow:function (index) {
            this.secEntity.specOptionList.splice(index,1);
        },
        save:function () {
            var url = '';
            var _this = this;
            //判断是更新还是插入
            if (this.secEntity.specification!=null){
                url = "/spec/update.do";
            }else {
                url ="/spec/save.do";
            }
            axios.post(url,this.secEntity).then(function (response) {
                if (response.data.success){
                    alert(response.data.message);
                    _this.secEntity.specOptionList = [];
                    _this.secEntity.specification = {};
                    _this.pageHandler(1);
                }else {
                    alert(response.data.message)
                }
            }).catch(function (reason) {
                console.log(reason);
            })
        },
        findSpecById:function (id) {
            var _this = this;
            //查询用户列表
            axios.get("/spec/findSpecById.do?id="+id).then(function (response) {
                _this.secEntity = response.data;
                console.log(response);
            }).catch(function (reason) {
                console.log(reason);
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
        deleteSpec:function () {
            var _this = this;
            //使用qs插件 处理数组
            axios.post('/spec/delete.do',Qs.stringify({ids: _this.selectedIds},{ indices: false }))
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
        }
        },
    created: function() {
        this.pageHandler(1);
    }

    })