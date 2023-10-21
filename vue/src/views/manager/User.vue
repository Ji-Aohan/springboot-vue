<template>
  <div>
    <div style="text-align: center">
      <el-input
        v-model="username"
        style="width: 20%"
        placeholder="用户名"
      ></el-input>
      <el-input
        v-model="name"
        style="width: 20%; margin-left: 10px"
        placeholder="姓名"
      ></el-input>
      <el-button @click="handleSelect" style="margin-left: 10px" type="primary"
        >查询</el-button
      >
      <el-button
        @click="handleReset"
        style="margin-left: 10px"
        type="success"
        plain
        >清空条件</el-button
      >
    </div>
    <div style="margin-top: 10px">
      <el-button type="primary" @click="handleAdd">新增</el-button>
      <el-button type="danger" @click="handleBatchDel">批量删除</el-button>
    </div>
    <el-table :data="userData" @selection-change="handleSelectionChange">
      <el-table-column type="selection"> </el-table-column>
      <el-table-column
        v-for="(val, index) in userColoms"
        :key="index"
        :label="val"
        :prop="index"
      >
      </el-table-column>
      <el-table-column label="头像">
        <template v-slot="scope">
          <el-image
            v-if="scope.row.avatar"
            style="width: 50px; height: 50px; border-radius: 50%"
            :src="scope.row.avatar"
          ></el-image>
        </template>
      </el-table-column>
      <el-table-column label="操作">
        <template v-slot="scope">
          <el-button type="primary" size="mini" @click="handleEdit(scope.row)"
            >编辑</el-button
          >
          <el-button type="danger" size="mini" @click="handleDel(scope.row.id)"
            >删除</el-button
          >
        </template>
      </el-table-column>
    </el-table>
    <div style="text-align: center; margin-top: 10px">
      <el-pagination
        @current-change="handleCurrentChange"
        :current-page="pageNum"
        :page-size="pageSize"
        layout="total, prev, pager, next"
        :total="total"
      >
      </el-pagination>
    </div>

    <el-dialog
      style="size: 50%"
      title="编辑资料"
      :visible.sync="EditFormVisible"
    >
      <el-form label-width="80px" :rules="rules" :model="form" ref="formRef">
        <el-form-item label="用户名:" prop="username">
          <el-input v-model="form.username"></el-input>
        </el-form-item>
        <el-form-item label="姓名:">
          <el-input v-model="form.name"></el-input>
        </el-form-item>
        <el-form-item label="邮箱:">
          <el-input v-model="form.email"></el-input>
        </el-form-item>
        <el-form-item label="地址:">
          <el-input v-model="form.address"></el-input>
        </el-form-item>
        <el-form-item label="角色">
          <el-radio-group v-model="form.role">
            <el-radio label="管理员"></el-radio>
            <el-radio label="用户"></el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="头像">
          <el-upload
            class="avatar-uploader"
            action="http://localhost:9090/file/upload"
            :headers="{ token: user.token }"
            list-type="picture"
            :on-success="handleAvatarSuccess"
          >
            <el-button type="primary">上传头像</el-button>
          </el-upload>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="handleCancel">取 消</el-button>
        <el-button type="primary" @click="handleConfirmEdit">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>
  
  <script>
export default {
  name: "User",
  data() {
    return {
      ids: [],
      form: {},
      EditFormVisible: false,
      total: 0,
      pageSize: 5,
      pageNum: 1,
      username: "",
      name: "",
      userData: [],
      user: JSON.parse(localStorage.getItem("honey-user") || "{}"),
      rules: {
        username: [{ required: true, message: "请输入账号", trigger: "blur" }],
      },
      userColoms: {
        id: "id",
        username: "用户名",
        name: "姓名",
        email: "邮箱",
        address: "地址",
      },
    };
  },
  created() {
    this.load();
  },
  methods: {
    handleSelectionChange(rows) {
      this.ids = rows.map((v) => v.id);
      console.log(this.ids);
    },
    handleBatchDel() {
      if (!this.ids.length) {
        this.$message.warning("请选择数据");
        return;
      }
      this.$confirm("确认批量删除吗?", "删除提示", { type: "warning" })
        .then(() => {
          this.$request
            .delete("/user/batchDelete", { data: this.ids })
            .then((res) => {
              if (res.code === "200") {
                // 表示操作成功
                this.$message.success("批量删除成功");
                this.load(1);
              } else {
                this.$message.error(res.msg); // 弹出错误的信息
              }
            });
        })
        .catch(() => {
          this.$message({
            type: "info",
            message: "已取消删除",
          });
        });
    },
    handleDel(id) {
      this.$confirm("确认删除这条记录吗?", "删除提示", { type: "warning" })
        .then(() => {
          this.$request.delete("/user/delete/" + id).then((res) => {
            if (res.code === "200") {
              // 表示操作成功
              this.$message.success("删除成功");
              this.load(1);
            } else {
              this.$message.error(res.msg); // 弹出错误的信息
            }
          });
        })
        .catch(() => {
          this.$message({
            type: "info",
            message: "已取消删除",
          });
        });
    },
    handleAvatarSuccess(response, file, fileList) {
      //把头像属性换成链接
      this.form.avatar = response.data;
    },
    handleConfirmEdit() {
      this.$refs.formRef.validate((valid) => {
        if (valid) {
          this.$request({
            url: this.form.id ? "/user/update" : "/user/add",
            method: this.form.id ? "PUT" : "POST",
            data: this.form,
          }).then((res) => {
            if (res.code === "200") {
              this.$message.success("编辑成功");
              this.load(1);
              this.EditFormVisible = false;
            } else {
              this.$message.error(res.msg);
            }
          });
        }
      });
    },
    handleAdd() {
      this.EditFormVisible = true;
      this.form = { role: "用户" };
    },
    handleCancel() {
      this.EditFormVisible = false;
      this.form = {};
    },
    handleEdit(row) {
      this.EditFormVisible = true;
      this.form = JSON.parse(JSON.stringify(row));
    },
    handleSelect() {
      this.load(1);
    },
    handleReset() {
      this.username = "";
      this.name = "";
      this.load();
    },
    handleCurrentChange(pageNum) {
      this.load(pageNum);
    },
    load(pageNum) {
      if (pageNum) {
        this.pageNum = pageNum;
      }
      this.$request
        .get("/user/selectByPage", {
          params: {
            pageNum: this.pageNum,
            pageSize: this.pageSize,
            username: this.username,
            name: this.name,
          },
        })
        .then((res) => {
          this.userData = res.data.records;
          this.total = res.data.total;
        });
    },
  },
};
</script>
  
  <style scoped>
</style>