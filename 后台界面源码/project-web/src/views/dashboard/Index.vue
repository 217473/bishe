<template>
    <!-- 设置el-main的高度绑定mianHeight变量，并以像素为单位 -->
    <el-main :style="{ height: mianHeight + 'px' }">
      <div style="display: flex;">
        <!-- 第一个卡片，包含柱状图 -->
        <el-card style="flex: 1;">
          <template #header>
            <div class="card-header">
              <span>订单统计</span>
            </div>
          </template>
          <div ref="myChart" :style="{ width: '400px', height: '300px' }"></div>
        </el-card>
        <!-- 第二个卡片，包含饼图 -->
        <el-card style="margin-left: 20px;flex: 1;">
          <template #header>
            <div class="card-header">
              <span>热门商品</span>
            </div>
          </template>
          <div ref="myChart1" :style="{ width: '400px', height: '300px' }"></div>
        </el-card>
        <!-- 第三个卡片，包含环图 -->
        <el-card style="margin-left: 20px;flex: 1;">
          <template #header>
            <div class="card-header">
              <span>最受欢迎</span>
            </div>
          </template>
          <div ref="myChart2" :style="{ width: '400px', height: '300px' }"></div>
        </el-card>
      </div>
    </el-main>
  </template>
  
  <script setup lang='ts'>
  import { ref, nextTick, onMounted, reactive } from 'vue'
  import useInstance from '@/hooks/useInstance';
  
  // 定义main容器的高度响应式变量，初始值为0
  const mianHeight = ref(0)
  // 获取全局对象
  const { global } = useInstance()
  // 分别定义三个图表对应的DOM引用
  const myChart = ref<HTMLElement>();
  const myChart1 = ref<HTMLElement>();
  const myChart2 = ref<HTMLElement>();
  
  // 柱状图初始化函数
  const charts1 = () => {
    // 使用ECharts初始化柱状图实例
    const echartInstance = global.$echarts.init(myChart.value);
    // 定义柱状图的配置项，使用响应式数据
    let option = reactive({
      xAxis: {
        type: 'category',
        data: ['Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat', 'Sun']
      },
      yAxis: {
        type: 'value'
      },
      series: [
        {
          data: [120, 200, 150, 80, 70, 110, 130],
          type: 'bar'
        }
      ]
    });
    //通过axios发送请求，获取数据，设置到上面的option的x轴数据和y轴数据
    // 设置柱状图的配置项
    echartInstance.setOption(option)
  }
  
  // 饼图初始化函数
  const charts2 = () => {
    // 使用ECharts初始化饼图实例
    const myChart = global.$echarts.init(myChart1.value);
    // 定义饼图的配置项，使用响应式数据
    let option = reactive({
      title: {
        subtext: 'Fake Data',
        left: 'center'
      },
      tooltip: {
        trigger: 'item'
      },
      legend: {
        orient:'vertical',
        left: 'left'
      },
      series: [
        {
          name: 'Access From',
          type: 'pie',
          radius: '50%',
          data: [
            { value: 1048, name: 'Search Engine' },
            { value: 735, name: 'Direct' },
            { value: 580, name: 'Email' },
            { value: 484, name: 'Union Ads' },
            { value: 300, name: 'Video Ads' }
          ],
          emphasis: {
            itemStyle: {
              shadowBlur: 10,
              shadowOffsetX: 0,
              shadowColor: 'rgba(0, 0, 0, 0.5)'
            }
          }
        }
      ]
    });
    //通过axios发送请求，获取数据，设置到上面的option的x轴数据和y轴数据
    // 设置饼图的配置项
    myChart.setOption(option)
  }
  
  // 环图初始化函数
  const charts3 = () => {
    // 使用ECharts初始化环图实例
    const myChart = global.$echarts.init(myChart2.value);
    // 定义环图的配置项，使用响应式数据
    let option = reactive({
      tooltip: {
        trigger: 'item'
      },
      legend: {
        top: '5%',
        left: 'center'
      },
      series: [
        {
          name: 'Access From',
          type: 'pie',
          radius: ['40%', '70%'],
          avoidLabelOverlap: false,
          label: {
            show: false,
            position: 'center'
          },
          emphasis: {
            label: {
              show: true,
              fontSize: '40',
              fontWeight: 'bold'
            }
          },
          labelLine: {
            show: false
          },
          data: [
            { value: 1048, name: 'Search Engine' },
            { value: 735, name: 'Direct' },
            { value: 580, name: 'Email' },
            { value: 484, name: 'Union Ads' },
            { value: 300, name: 'Video Ads' }
          ]
        }
      ]
    });
    //通过axios发送请求，获取数据，设置到上面的option的x轴数据和y轴数据
    // 设置环图的配置项
    myChart.setOption(option)
  }
  
  // 组件挂载完成后的钩子函数
  onMounted(() => {
    // 依次调用三个图表的初始化函数
    charts1();
    charts2();
    charts3();
    // 等待DOM更新后，设置main容器的高度为窗口高度减去100像素
    nextTick(() => {
      mianHeight.value = window.innerHeight - 100
    })
  })
  </script>
  
  <style scoped lang='scss'>
  /* 这里可以编写局部作用域的SCSS样式 */
  </style>