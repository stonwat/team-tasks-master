<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">

      <el-form-item label="数据时间" prop="dataTime">
        <el-date-picker clearable
          v-model="queryParams.dataTime"
          type="date"
          value-format="yyyy-MM-dd"
          placeholder="请选择数据时间">
        </el-date-picker>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="primary"
          plain
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
          v-hasPermi="['system:digitalemployee:add']"
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
          v-hasPermi="['system:digitalemployee:edit']"
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
          v-hasPermi="['system:digitalemployee:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['system:digitalemployee:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="digitalemployeeList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="使用AI自动生成的采购方案量" align="center" prop="orderError1" />
      <el-table-column label="采购方案总量" align="center" prop="orderComplete1" />
      <el-table-column label="应用深度现状（百分比）" align="center" prop="orderPersent" />
      <el-table-column label="集团内排名" align="center" prop="orderRank" />
      <el-table-column label="单项赋分" align="center" prop="orderScore" />
      <el-table-column label="数据时间" align="center" prop="dataTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.dataTime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="备注" align="center" prop="remark" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['system:digitalemployee:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['system:digitalemployee:remove']"
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

    <!-- 添加或修改digitalemployee对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="使用AI自动生成的采购方案量" prop="orderError1">
          <el-input v-model="form.orderError1" placeholder="请输入使用AI自动生成的采购方案量" />
        </el-form-item>
        <el-form-item label="采购方案总量" prop="orderComplete1">
          <el-input v-model="form.orderComplete1" placeholder="请输入采购方案总量" />
        </el-form-item>
        <el-form-item label="应用深度现状（百分比）" prop="orderPersent">
          <el-input v-model="form.orderPersent" placeholder="请输入应用深度现状（百分比）" />
        </el-form-item>
        <el-form-item label="集团内排名" prop="orderRank">
          <el-input v-model="form.orderRank" placeholder="请输入集团内排名" />
        </el-form-item>
        <el-form-item label="单项赋分" prop="orderScore">
          <el-input v-model="form.orderScore" placeholder="请输入单项赋分" />
        </el-form-item>
        <el-form-item label="数据时间" prop="dataTime">
          <el-date-picker clearable
            v-model="form.dataTime"
            type="date"
            value-format="yyyy-MM-dd"
            placeholder="请选择该月最后一天">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input v-model="form.remark" placeholder="请输入备注" />
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
import { listDigitalemployee, getDigitalemployee, delDigitalemployee, addDigitalemployee, updateDigitalemployee } from "@/api/system/digitalemployee";

export default {
  name: "Digitalemployee",
  dicts: ['employee_type'],
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
      // digitalemployee表格数据
      digitalemployeeList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        orderError1: null,
        orderComplete1: null,
        orderError2: null,
        orderComplete2: null,
        orderError3: null,
        orderComplete3: null,
        orderError4: null,
        orderComplete4: null,
        orderError5: null,
        orderComplete5: null,
        orderRank: null,
        orderScore: null,
        orderPersent: null,
        employeeType: null,
        dataTime: null,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
      }
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询digitalemployee列表 */
    getList() {
      this.loading = true;
      this.queryParams.employeeType = '13'
      listDigitalemployee(this.queryParams).then(response => {
        this.digitalemployeeList = response.rows;
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
        orderError1: null,
        orderComplete1: null,
        orderError2: null,
        orderComplete2: null,
        orderError3: null,
        orderComplete3: null,
        orderError4: null,
        orderComplete4: null,
        orderError5: null,
        orderComplete5: null,
        orderRank: null,
        orderScore: null,
        orderPersent: null,
        employeeType: null,
        dataTime: null,
        remark: null
      };
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
      this.title = "添加digitalemployee";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids
      getDigitalemployee(id).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改digitalemployee";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
                    this.form.employeeType = '13'
          if (this.form.id != null) {
            updateDigitalemployee(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addDigitalemployee(this.form).then(response => {
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
      this.$modal.confirm('是否确认删除digitalemployee编号为"' + ids + '"的数据项？').then(function() {
        return delDigitalemployee(ids);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    /** 导出按钮操作 */
    handleExport() {
      const filename = `采供智能体数据${new Date().getTime()}.xlsx`;
      this.download('system/digitalemployee/export', {
        ...this.queryParams
      }, filename);
    }
  }
};
</script>
