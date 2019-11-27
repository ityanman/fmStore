new Vue({
    el:"#app",
    data:{
        categoryList1:[],//分类1数据列表
        categoryList2:[],//分类2数据列表
        categoryList3:[],//分类3数据列表
        grade:1,  //记录当前级别
        catSelected1:-1,
        catSelected2:-1,
        catSelected3:-1,
        typeId:0,
        brandList:[],
        selBrand:-1,
        curImageObj:{
            color:'',
            url:''
        },
        imageList:[]
    },
    methods:{
        loadCateData: function (id) {
            var _this = this;
            axios.post("/itemCat/findByParentId.do?parentId="+id)
                .then(function (response) {
                    if (_this.grade == 1){
                        //取服务端响应的结果
                        _this.categoryList1 = response.data;
                    }
                    if (_this.grade == 2){
                        //取服务端响应的结果
                        _this.categoryList2 =response.data;
                    }
                    if (_this.grade == 3){
                        //取服务端响应的结果
                        _this.categoryList3 =response.data;
                    }
                }).catch(function (reason) {
                console.log(reason);
            })
        },
        getCategorySel:function (grade) {
            if (grade==1){
                this.categoryList2 = [];
                this.categoryList3 = [];
                this.grade = grade+1;
                this.loadCateData(this.catSelected1);
            }
            if (grade==2){
                this.categoryList3 = [];
                this.grade = grade+1;
                this.loadCateData(this.catSelected2);
            }
            if (grade==3){
                var _this = this;
                axios.get("/itemCat/findOneCategory.do?id="+this.catSelected3).then(function (value) {
                    _this.typeId = value.data.typeId;
                    console.log(value);
                }).catch(function (reason) {
                    console.log(reason)
                })
            }
        },
        uploadFile:function () {
            var _this = this;
            var formData = new FormData();
            formData.append('file', file.files[0])
            var instance=axios.create({
                withCredentials: true
            });
            instance.post('/upload/uploadFile.do', formData).then(function (response) {
                _this.curImageObj.url=response.data.message;
                console.log(response.data);
            }).catch(function (reason) {
                console.log(reason);
            });
        },
        imageSave:function () {
            var _this = this;
            if (_this.curImageObj.color =='' || _this.curImageObj.url==''){
                alert("请输入颜色或上传图片");
                return;
            }
            var imgObj = {color:_this.curImageObj.color,url:_this.curImageObj.url};
            _this.imageList.push(imgObj);
            _this.curImageObj.url='';
            _this.curImageObj.color='';
        },
        deleteImg:function (url,index) {
            var _this  = this;
            axios.get("/upload/deleteImg.do?url="+url).then(function (response) {
                 if (response.data.success){
                     _this.imageList.splice(index,1)
                     alert(response.data.message);
                 }else {
                     alert(response.data.message)
                 }
            }).catch(function (reason) {
                alert(reason)
            })
        }
        },
    watch: { //监听属性的变化
        typeId:function(newValue, oldValue) {
            var _this = this;
            _this.brandList =[];
            _this.selBrand = -1;
            axios.post("/temp/findOne.do?id="+newValue)
                .then(function (response) {
                    console.log(response.data);
                    _this.brandList = JSON.parse(response.data.brandIds);
                    console.log(_this.brandList);
                }).catch(function (reason) {
                console.log(reason);
            });
        }
    },
    created: function() {
        this.loadCateData(0);
    }
});
