Vue.component('v-select', VueSelect.VueSelect);
new Vue({
    el:"#app",
    data:{
        tempList:[],
        temp:{},
        searchTemp:{},
        page: 1,  //显示的是哪一页
        pageSize: 10, //每一页显示的数据条数
        total: 150, //记录总数
        maxPage:9,

        placeholder: '可以进行多选',
        brandsOptions: [],
        selectBrands: [],
        sel_brand_obj: [],
        addname:"",
        ids:[],
        id:null,


        specOptions: [],
        selectSpecs: [],
        sel_spec_obj: []
    },
    methods:{
        pageHandler: function (page) {
            var _this = this;
            this.page = page;
            axios.post("/temp/search.do?page="+page+"&pageSize="+this.pageSize,this.searchTemp)
                .then(function (response) {
                    //取服务端响应的结果
                    _this.tempList = response.data.rows;
                    _this.total = response.data.total;
                    console.log(response.data);
                }).catch(function (reason) {
                console.log(reason);
            })
        },
        // 定义方法：获取JSON字符串中的某个key对应值的集合
        jsonToStr:function (jsonStr,key) {
            var jsonObj = JSON.parse(jsonStr);
            var value = "";
            for (var i = 0;i<jsonObj.length;i++){
                if (i>0){
                    value += ",";
                }
                value += jsonObj[i][key];
            }
            return value;
        },
        selected_brand: function(values){
/*            this.selectBrands =values.map(function(obj){
                return obj.id
            });*/
       /*     console.log(this.sel_brand_obj)*/
        },
        selected_spec: function(values){
            this.selectSpecs =values.map(function(obj){
                return obj.id
            });
            console.log(this.sel_spec_obj);
        },

        selLoadData:function () {
            var _this = this;
            axios.get("/brand/selectOptionList.do")
                .then(function (response) {
                    _this.brandsOptions = response.data;
                }).catch(function (reason) {
                console.log(reason);
            });
            axios.get("/spec/selectOptionList.do")
                .then(function (response) {
                    _this.specOptions = response.data;
                }).catch(function (reason) {
                console.log(reason);
            });
        },
        save:function () {
            var _this = this;
            //判断是修改还是更新
            var url = "";
            if (_this.id != null){
               url = "/temp/update.do";
            }else {
                url = "/temp/add.do";
            }
            var entity = {

                name:_this.addname,
                specIds:_this.sel_spec_obj,
                brandIds:_this.sel_brand_obj,
                id:_this.id
            };
            axios.post(url,entity).then(function (value) {
                if (value.data.success){
                    alert(value.data.message)
                            _this.pageHandler(1)
                    console.log(value)
                }else {
                    alert(value.data.message)
                }
            }).catch(function (reason) {
                console.log(reason)
            })

        },

        findSpecById:function (id) {
            var _this  = this;
            axios.get("/temp/findTempById.do?id="+id).then(function (value) {
                var specIds = _this.jsonToStr2(value.data.specIds)
                _this.sel_spec_obj = specIds;
                var brands = _this.jsonToStr2(value.data.brandIds)
                _this.sel_brand_obj =brands;
                _this.addname  = value.data.name;
                _this.id = id;
            })
        },
        //字符串转成json对象
        jsonToStr2:function (jsonStr) {
           return  JSON.parse(jsonStr);
        },

        //点击新建时清除表单中的内容
        clean:function () {
            var _this = this;
                _this.selectBrands=[],
                _this.sel_brand_obj= [],
                _this.addname="",
                _this.selectSpecs=[],
                _this.sel_spec_obj=[],
                _this.id=null
        },

        //监听复选框的勾选
        deleteSelection:function (event,id) {
            //复选框选中
            if (event.target.checked){
                //选中
                this.ids.push(id)
            }else {
                //取消选中
                //移除选中元素
                var idx = this.ids.indexOf(id);
                this.ids.splice(idx);
            }
        },
        deleteTemp:function () {
            var _this = this;
            axios.post("/temp/delete.do",Qs.stringify({ids:_this.ids},{ indices: false })).then(function (value) {
                if (value.data.success){
                    alert(value.data.message)
                    _this.pageHandler(1);
                }else {
                    alert(value.data.message);
                }
            }).catch(function (reason) {
                alert(value.data.message);
            })
        }

    },
    created: function() {
        this.pageHandler(1);
        this.selLoadData();
    }
});