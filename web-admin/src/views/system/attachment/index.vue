<template>
  <div class="app-container">
    <!-- 搜索表单部分 (未变动) -->
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="名称" prop="name">
        <el-input
          v-model="queryParams.name"
          placeholder="请输入名称"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="分类" prop="type">
        <el-select v-model="queryParams.type" placeholder="请选择分类" clearable>
          <el-option
            v-for="dict in dict.type.attachment"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="详情" prop="info">
        <el-input
          v-model="queryParams.info"
          placeholder="请输入详情"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="链接" prop="url">
        <el-input
          v-model="queryParams.url"
          placeholder="请输入链接"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="时间" prop="time">
        <el-date-picker clearable
                        v-model="queryParams.time"
                        type="date"
                        value-format="yyyy-MM-dd"
                        placeholder="请选择时间">
        </el-date-picker>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <!-- 按钮操作栏 (未变动) -->
    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="primary"
          plain
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
          v-hasPermi="['system:attachment:add']"
        >新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="el-icon-edit"
          size="mini"
          :disabled="single"
          @click="handleUpdate"
          v-hasPermi="['system:attachment:edit']"
        >修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="el-icon-delete"
          size="mini"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['system:attachment:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['system:attachment:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <!-- 表格部分 (增加了一点预览功能) -->
    <el-table v-loading="loading" :data="attachmentList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="ID" align="center" prop="id" />
      <el-table-column label="名称" align="center" prop="name" />
      <el-table-column label="分类" align="center" prop="type">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.attachment" :value="scope.row.type"/>
        </template>
      </el-table-column>
      <el-table-column label="详情" align="center" prop="info" show-overflow-tooltip/>

      <!-- 修改：链接列增加简单的预览/跳转 -->
      <el-table-column label="链接/预览" align="center" prop="url">
        <template slot-scope="scope">
          <!-- 如果是图片，显示缩略图 -->
          <el-image
            v-if="checkIsImage(scope.row.url)"
            style="width: 50px; height: 50px"
            :src="scope.row.url"
            :preview-src-list="[scope.row.url]">
          </el-image>
          <!-- 否则显示链接文本 -->
          <el-link v-else :href="scope.row.url" target="_blank" type="primary">查看文件</el-link>
        </template>
      </el-table-column>

      <el-table-column label="时间" align="center" prop="time" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.time, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['system:attachment:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['system:attachment:remove']"
          >删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination
      v-show="total>0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />

    <!-- 添加或修改对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="600px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">

        <!-- 1. 优先选择类型，以便控制上传格式 -->
        <el-form-item label="分类" prop="type">
          <el-select v-model="form.type" placeholder="请选择分类" @change="handleTypeChange">
            <el-option
              v-for="dict in dict.type.attachment"
              :key="dict.value"
              :label="dict.label"
              :value="dict.value"
            ></el-option>
          </el-select>
        </el-form-item>

        <el-form-item label="名称" prop="name">
          <el-input v-model="form.name" placeholder="请输入名称 (上传文件可自动回填)" />
        </el-form-item>

        <el-form-item label="详情" prop="info">
          <el-input v-model="form.info" type="textarea" placeholder="请输入详情" />
        </el-form-item>

        <!-- 2. 修改链接输入框为上传组件 -->
        <el-form-item label="上传" prop="url">
          <!-- 只有选了类型才能上传，提升体验 -->
          <div v-if="!form.type" style="color: #999; font-size: 12px;">请先选择上方的“分类”</div>

          <el-upload
            v-else
            ref="upload"
            :action="uploadUrl"
            :headers="headers"
            :limit="1"
            :on-exceed="handleExceed"
            :on-success="handleUploadSuccess"
            :on-remove="handleRemove"
            :before-upload="beforeUpload"
            :file-list="fileList"
            :accept="fileAccept"
            list-type="picture-card"
            :class="{hideUpload: fileList.length >= 1}"
          >
            <!-- 样式调整：如果选的是视频，picture-card 可能不太好看，这里通用处理 -->
            <i class="el-icon-plus"></i>
            <div slot="tip" class="el-upload__tip">
              只能上传 {{ form.type === '1' ? '图片' : '视频' }} 文件
              {{ form.type === '1' ? '，且不超过 5MB' : '，且不超过 50MB' }}
            </div>
          </el-upload>

          <!-- 保留一个输入框用于显示URL，或者允许手动输入 -->
          <el-input v-model="form.url" placeholder="上传后自动生成，也可输入外部链接" style="margin-top: 5px;" />
        </el-form-item>

        <el-form-item label="时间" prop="time">
          <el-date-picker clearable
                          v-model="form.time"
                          type="date"
                          value-format="yyyy-MM-dd"
                          placeholder="请选择时间">
          </el-date-picker>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listAttachment, getAttachment, delAttachment, addAttachment, updateAttachment } from "@/api/system/attachment";
// 1. 引入 Token 工具
import { getToken } from "@/utils/auth";

export default {
  name: "Attachment",
  dicts: ['attachment'],
  data() {
    return {
      // 遮罩层
      loading: true,
      // 选中数组
      ids: [],
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // 文件类型表格数据
      attachmentList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        name: null,
        type: null,
        info: null,
        url: null,
        time: null,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        name: [{ required: true, message: "名称不能为空", trigger: "blur" }],
        type: [{ required: true, message: "请选择分类", trigger: "change" }],
        url: [{ required: true, message: "链接/文件不能为空", trigger: "blur" }]
      },

      // 2. 新增上传相关变量
      // 上传接口地址，假设使用 RuoYi 通用上传接口
      uploadUrl: process.env.VUE_APP_BASE_API + "/common/upload",
      // 设置上传的请求头部
      headers: { Authorization: "Bearer " + getToken() },
      // 上传文件列表
      fileList: []
    };
  },
  computed: {
    // 3. 计算属性：根据选择的类型控制上传组件接受的文件格式
    fileAccept() {
      if (this.form.type === '1') { // 1是图片
        return ".jpg, .jpeg, .png, .gif";
      } else if (this.form.type === '2') { // 2是视频
        return ".mp4, .avi, .mov";
      }
      return "*";
    }
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询列表 */
    getList() {
      this.loading = true;
      listAttachment(this.queryParams).then(response => {
        this.attachmentList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    // 取消按钮
    cancel() {
      this.open = false;
      this.reset();
    },
    // 表单重置
    reset() {
      this.form = {
        id: null,
        name: null,
        type: null, // 默认为空或设置默认值
        info: null,
        url: null,
        time: null,
        createBy: null,
        createTime: null
      };
      // 重置文件列表
      this.fileList = [];
      this.resetForm("form");
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1;
      this.getList();
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.resetForm("queryForm");
      this.handleQuery();
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.id)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加文件类型";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids
      getAttachment(id).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改文件类型";

        // 4. 处理回显：如果有 url，构造 fileList
        if (this.form.url) {
          this.fileList = [{
            name: this.form.name || '已上传文件',
            url: this.form.url
          }];
        }
      });
    },

    // --- 5. 上传相关方法开始 ---

    // 切换类型时，清空当前文件列表（因为类型不匹配了）
    handleTypeChange() {
      if(this.fileList.length > 0) {
        this.$confirm('切换类型将清空已上传的文件, 是否继续?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          this.fileList = [];
          this.form.url = '';
        }).catch(() => {
          // 恢复之前的选项(这里简化处理，实际可能需要记录oldValue)
        });
      }
    },

    // 上传前校验
    beforeUpload(file) {
      if (!this.form.type) {
        this.$message.warning("请先选择分类！");
        return false;
      }

      const isTypeCorrect = this.checkFileType(file, this.form.type);
      let isSizeCorrect = true;

      // 图片限制 5MB，视频限制 50MB
      if (this.form.type === '1') {
        isSizeCorrect = file.size / 1024 / 1024 < 5;
        if (!isSizeCorrect) this.$message.error('上传图片大小不能超过 5MB!');
      } else {
        isSizeCorrect = file.size / 1024 / 1024 < 50;
        if (!isSizeCorrect) this.$message.error('上传视频大小不能超过 50MB!');
      }

      return isTypeCorrect && isSizeCorrect;
    },

    // 检查文件类型辅助函数
    checkFileType(file, type) {
      if (type === '1') { // 图片
        if (file.type.indexOf('image') === -1) {
          this.$message.error('请上传图片格式文件！');
          return false;
        }
      } else if (type === '2') { // 视频
        if (file.type.indexOf('video') === -1) {
          this.$message.error('请上传视频格式文件！');
          return false;
        }
      }
      return true;
    },

    // 上传成功回调
    handleUploadSuccess(res, file) {
      if (res.code === 200) { // 假设后端返回 code 200 为成功
        this.form.url = res.url || res.fileName; // 根据后端实际返回字段调整
        this.$message.success("上传成功");

        // 如果名称为空，自动填充文件名
        if(!this.form.name) {
          this.form.name = res.originalFilename || file.name;
        }

        // 更新 fileList 状态
        this.fileList = [{ name: file.name, url: this.form.url }];
      } else {
        this.$message.error(res.msg || "上传失败");
        this.fileList = []; // 失败清空列表
      }
    },

    // 移除文件回调
    handleRemove(file, fileList) {
      this.form.url = '';
      this.fileList = [];
    },

    // 文件超出限制
    handleExceed(files, fileList) {
      this.$message.warning(`当前限制选择 1 个文件，请删除后重新上传`);
    },

    // 列表页辅助：判断是否是图片链接
    checkIsImage(url) {
      if (!url) return false;
      const ext = url.substring(url.lastIndexOf('.') + 1).toLowerCase();
      return ['jpg', 'jpeg', 'png', 'gif', 'bmp'].includes(ext);
    },

    // --- 上传相关方法结束 ---

    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateAttachment(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addAttachment(this.form).then(response => {
              this.$modal.msgSuccess("新增成功");
              this.open = false;
              this.getList();
            });
          }
        }
      });
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const ids = row.id || this.ids;
      this.$modal.confirm('是否确认删除数据项？').then(function() {
        return delAttachment(ids);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('system/attachment/export', {
        ...this.queryParams
      }, `attachment_${new Date().getTime()}.xlsx`)
    }
  }
};
</script>

<style scoped>
/* 样式：当有文件时隐藏上传框的加号，只显示图片/文件，避免样式错乱（可选） */
::v-deep .hideUpload .el-upload--picture-card {
  display: none;
}
</style>
