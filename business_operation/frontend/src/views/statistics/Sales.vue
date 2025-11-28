<template>
  <div class="sales-statistics">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>销售统计</span>
          <div>
            <el-radio-group v-model="timeRange" @change="handleTimeRangeChange">
              <el-radio-button label="day">今日</el-radio-button>
              <el-radio-button label="week">本周</el-radio-button>
              <el-radio-button label="month">本月</el-radio-button>
              <el-radio-button label="year">本年</el-radio-button>
            </el-radio-group>
            <el-button style="margin-left: 10px;" @click="handleExport">
              <el-icon><Download /></el-icon>
              导出Excel
            </el-button>
          </div>
        </div>
      </template>

      <el-row :gutter="20" class="summary-row">
        <el-col :span="6">
          <div class="summary-item">
            <p class="summary-label">销售额</p>
            <p class="summary-value">¥123,456.78</p>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="summary-item">
            <p class="summary-label">订单量</p>
            <p class="summary-value">1,234</p>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="summary-item">
            <p class="summary-label">客单价</p>
            <p class="summary-value">¥100.05</p>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="summary-item">
            <p class="summary-label">退单率</p>
            <p class="summary-value">2.5%</p>
          </div>
        </el-col>
      </el-row>

      <div id="salesTrendChart" style="height: 400px; margin-top: 20px;"></div>

      <el-divider />

      <h3 style="margin-bottom: 15px;">商品销售TOP10</h3>
      <el-table :data="topProducts" border stripe>
        <el-table-column type="index" label="排名" width="60" />
        <el-table-column prop="productName" label="商品名称" min-width="200" />
        <el-table-column prop="salesVolume" label="销量" width="100" />
        <el-table-column prop="salesAmount" label="销售额" width="120">
          <template #default="{ row }">
            ¥{{ row.salesAmount }}
          </template>
        </el-table-column>
      </el-table>
    </el-card>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { ElMessage } from 'element-plus'

const timeRange = ref('day')
const topProducts = ref([])

const handleTimeRangeChange = () => {
  ElMessage.info('加载' + timeRange.value + '数据')
}

const handleExport = () => {
  ElMessage.success('导出成功')
}
</script>

<style scoped>
.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.summary-row {
  padding: 20px 0;
}

.summary-item {
  text-align: center;
  padding: 20px;
  background: #f5f7fa;
  border-radius: 8px;
}

.summary-label {
  font-size: 14px;
  color: #909399;
  margin: 0 0 10px 0;
}

.summary-value {
  font-size: 24px;
  font-weight: bold;
  color: #303133;
  margin: 0;
}
</style>

