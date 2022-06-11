<template>
  <div class="report-summary">
    <GlobalHeader />
    <div class="page">
      <!-- 快捷键：(div*factor>p.name+div.content)*5   -->
      <div class="return-page-header flex-vc" @click="router.go(-1)">
        <el-icon>
          <ArrowLeftBold></ArrowLeftBold>
        </el-icon>
        <span>众测详情页</span>
      </div>
      <p class="title">众测报告分析</p>
      <div class="factor">
        <p class="name">1.报告聚类</p>
        <div class="flex-hc mt-32">
          <el-radio-group v-model="kValue" @change="handleKValueChange">
            <el-radio label="2" size="large">K=2</el-radio>
            <el-radio v-if="reportCount>=3" label="3" size="large">K=3</el-radio>
            <el-radio v-if="reportCount>=4" label="4" size="large">K=4</el-radio>
          </el-radio-group>
        </div>
        <div ref="clusteringChartRef" class="self-center" style="width: 500px;height: 400px"></div>
      </div>
      <div class="factor">
        <p class="name">2.报告分类</p>
        <div ref="classificationChartRef" class="self-center" style="width: 500px;height: 400px"></div>
      </div>
      <div class="factor">
        <p class="name">3.报告排序</p>
        <div ref="sortChartRef" class="self-center" style="width: 500px;height: 400px"></div>
      </div>
      <div class="factor">
        <p class="name mb-16">4.报告质量筛选</p>
        <div class="test-quality-container mb-32">
          <TestQualityCard v-for="(item,index) in testQualityList"
                           :key="index"
                           :avatar="item.avatar"
                           :nickname="item.nickname"
                           :score="item.score"
                           @click="toReportDetailPage(item.testId)" />
        </div>
      </div>
      <div class="factor">
        <p class="name">5.报告融合</p>
        <p class="sub-name">5.1词云绘制</p>
        <div class="word-cloud flex-hc mb-32">
          <div ref="analysisChartRef" style="width: 400px;height: 240px"></div>
          <div ref="conclusionChartRef" style="width: 400px;height: 240px"></div>
        </div>
        <p class="sub-name">5.2测试环境及工具统计</p>
        <div class="pie-charts flex-hc mb-32">
          <div ref="environmentChartRef" style="width: 400px;height: 300px"></div>
          <div ref="toolChartRef" style="width: 400px;height: 300px"></div>
        </div>
        <p class="sub-name">5.3测试用例统计</p>
        <div ref="caseChartRef" class="self-center" style="width: 500px;height: 400px"></div>
        <p class="sub-name">5.4报告关系图</p>
        <div ref="relationChartRef" class="self-center" style="width: 500px;height: 400px"></div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: "ReportSummary",
};
</script>
<script setup>
import GlobalHeader from "@/components/GlobalHeader";
import { createChartsInstance } from "@/plugins/echarts";
import { ScatterChart, SunburstChart, BarChart, PieChart, GraphChart } from "echarts/charts";
import TestQualityCard from "@/components/TestQualityCard";
import { onMounted, ref, defineProps, reactive } from "vue";
import "echarts-wordcloud";
import * as echarts from "echarts/core";
import { useRouter } from "vue-router";
import {
  getClassifyReport,
  getClusterReport,
  getFilterReport, getFinishedReportCount,
  getIntegrateReport,
  getSortReport,
} from "@/api/reportSummary";
// import router from "@/router";

const clusteringChartRef = ref(null);
let clusteringChart = null;
const classificationChartRef = ref(null);
let classificationChart = null;
const sortChartRef = ref(null);
let sortChart = null;
const analysisChartRef = ref(null);
let analysisChart = null;
const conclusionChartRef = ref(null);
let conclusionChart = null;
const environmentChartRef = ref(null);
let environmentChart = null;
const toolChartRef = ref(null);
let toolChart = null;
const caseChartRef = ref(null);
let caseChart = null;
const relationChartRef = ref(null);
// eslint-disable-next-line no-unused-vars
let relationChart = null;

const router = useRouter();
const props = defineProps({
  taskId: {
    type: String,
    default: "",
  },
});
onMounted(() => {
  clusteringChart = createChartsInstance(ScatterChart, clusteringChartRef);
  classificationChart = createChartsInstance(SunburstChart, classificationChartRef);
  sortChart = createChartsInstance(BarChart, sortChartRef);
  analysisChart = echarts.init(analysisChartRef.value);
  conclusionChart = echarts.init(conclusionChartRef.value);
  environmentChart = createChartsInstance(PieChart, environmentChartRef);
  toolChart = createChartsInstance(PieChart, toolChartRef);
  caseChart = createChartsInstance(BarChart, caseChartRef);
  relationChart = createChartsInstance(GraphChart, relationChartRef);

  //调用报告的接口
  //聚类
  fetchClusterReport(props.taskId, kValue.value);
  //分类
  fetchClassifyReport(props.taskId);
  //排序
  fetchSortReport(props.taskId);
  //质量筛选
  fetchFilterReport(props.taskId);
  //融合
  fetchIntegrateReport(props.taskId);
  //获取报告人数
  fetchFinishedReportCount(props.taskId);
});

//跳转至报告详情页相关
const toReportDetailPage = function(testId) {
  router.push({ name: "report-detail", params: { testId } });
};
//报告质量筛选相关
const testQualityList = reactive([]);
const fetchFilterReport = function(taskId) {
  testQualityList.splice(0, testQualityList.length);
  getFilterReport({ taskId }).then(response => {
    response.data.forEach(item => {
      testQualityList.push(item);
    });
  });
};


//聚类图相关

//聚类图需要获取报告人数
const reportCount = ref(0);
const fetchFinishedReportCount = function(taskId) {
  getFinishedReportCount({ taskId }).then(response => {
    reportCount.value = response.data;
  });
};
const kValue = ref("2");
const handleKValueChange = function(kValue) {
  fetchClusterReport(props.taskId, kValue);
};
const fetchClusterReport = function(taskId, k) {
  getClusterReport({ taskId, k }).then(response => {
    drawClusteringChart(response.data, k);
  });
};
const drawClusteringChart = function(clusterData, k) {
  let CLUSTER_COUNT = k;
  let COLOR_ALL = [
    "#37A2DA",
    "#e06343",
    "#37a354",
    "#b55dba",
    "#b5bd48",
    "#8378EA",
    "#96BFFF",
  ];
  //作为参数传入到方法中
  const pieces = new Array(parseInt(k)).fill(0).map((item, index) => {
    return {
      value: index,
      label: `C${index + 1}`,
      color: COLOR_ALL[index],
    };
  });

  const option = {
    title: {
      text: "报告聚类散点图",
      left: "center",
      top: "bottom",
      textStyle: {
        fontWeight: 400,
        fontSize: "14px",
      },
    },
    animationDuration: 1500,
    animationEasingUpdate: "quinticInOut",
    dataset: {
      source: clusterData,
    },
    tooltip: {
      position: "top",
      formatter: function(params) {
        return params.marker + params.data.nickname;
      },
    },
    visualMap: {
      type: "piecewise",
      top: "middle",
      min: 0,
      max: CLUSTER_COUNT,
      left: "left",
      //设置了pieces，则number无效
      // splitNumber: CLUSTER_COUNT,
      //那个维度映射到视觉元素上
      dimension: "clusteringType",
      //分段
      pieces: pieces,
    },
    grid: {
      left: 120,
    },
    xAxis: {},
    yAxis: {},
    series: {
      type: "scatter",
      //设置x轴和y轴对应的映射维度
      encode: { x: "xaxis", y: "yaxis" },
      symbolSize: 10,
      // itemStyle: {
      //   borderColor: "#555",
      // },
    },
  };
  clusteringChart.setOption(option);
  //在这里定义图表点击事件
  clusteringChart.on("click", function(params) {
    const { testId } = params.data;
    toReportDetailPage(testId);
  });
};


//分类图相关
const fetchClassifyReport = function(taskId) {
  getClassifyReport({ taskId }).then(response => {
    drawClassificationChart(response.data);
  });
};
const drawClassificationChart = function(data) {
  const option = {
    title: {
      text: "报告分类饼状图",
      left: "center",
      top: "bottom",
      textStyle: {
        fontWeight: 400,
        fontSize: "14px",
      },

    },
    series: {
      type: "sunburst",
      data: data,
      radius: [0, "95%"],
      sort: undefined,
      emphasis: {
        focus: "ancestor",
      },
      levels: [
        {},
        {
          r0: "15%",
          r: "35%",
          itemStyle: {
            borderWidth: 2,
          },
          label: {
            rotate: "tangential",
          },
        },
        {
          r0: "35%",
          r: "70%",
          label: {
            align: "right",
          },
        },
        {
          r0: "70%",
          r: "72%",
          label: {
            position: "outside",
            padding: 3,
            silent: false,
          },
          itemStyle: {
            borderWidth: 3,
          },
        },
      ],
    },
  };
  classificationChart.setOption(option);
  //在这里定义图表点击事件
  classificationChart.on("click", function(params) {
    const { testId } = params.data;
    toReportDetailPage(testId);
  });

};

//报告排序图相关
const fetchSortReport = function(taskId) {
  getSortReport({ taskId }).then(response => {
    drawSortChart(response.data);
  });
};
const drawSortChart = function(data) {
  const option = {
    title: {
      text: "报告排序图",
      left: "center",
      top: "bottom",
      textStyle: {
        fontWeight: 400,
        fontSize: "14px",
      },
    },
    dataset: {
      source: data,
    },
    tooltip: {
      left: "center",
      // formatter: function(params) {
      //   return params.marker + params.data[4];
      // },
    },
    grid: { containLabel: true },
    xAxis: { name: "score" },
    yAxis: {
      type: "category",
      name: "worker",
    },
    visualMap: {
      type: "continuous",
      orient: "vertical",
      top: "top",
      min: 0,
      max: 10.0,
      text: ["High", "Low"],
      // Map the score column to color
      dimension: "score",
      inRange: {
        color: ["#65B581", "#FFCE34", "#FD665F"],
      },
    },
    series:
      {
        type: "bar",
        encode: {
          x: "score",
          y: "nickname",
        },
      },
  };
  sortChart.setOption(option);
  sortChart.on("click", function(params) {
    const { testId } = params.data;
    toReportDetailPage(testId);
  });
};

//报告融合相关
const fetchIntegrateReport = function(taskId) {
  getIntegrateReport({ taskId }).then((response) => {
    const { caseStatistics, defectWordCloud, suggestWordCloud, testEnv, testTool, reportRelation } = response.data;
    drawCaseChart(caseStatistics);
    drawEnvironmentChart(testEnv);
    drawToolChart(testTool);
    drawAnalysisChart(defectWordCloud);
    drawConclusionChart(suggestWordCloud);
    drawRelationChart(reportRelation);
  });
};
//报告融合-缺陷分析
const drawAnalysisChart = function(data) {
  analysisChart.setOption({
    title: {
      text: "缺陷分析词云",
      left: "center",
      top: "bottom",
      textStyle: {
        fontWeight: 400,
        fontSize: "14px",
      },
    },
    series: {
      type: "wordCloud",
      shape: "cardioid",

      keepAspect: false,
      right: null,
      bottom: null,
      sizeRange: [12, 60],
      rotationRange: [-90, 90],
      rotationStep: 45,
      gridSize: 8,
      drawOutOfBound: false,
      layoutAnimation: true,
      textStyle: {
        fontFamily: "sans-serif",
        fontWeight: "bold",
        color: function() {
          // Random color
          return "rgb(" + [
            Math.round(Math.random() * 160),
            Math.round(Math.random() * 160),
            Math.round(Math.random() * 160),
          ].join(",") + ")";
        },
      },
      emphasis: {
        focus: "self",
        textStyle: {
          textShadowBlur: 10,
          textShadowColor: "#C0C0D0",
        },
      },

      // Data is an array. Each array item must have name and value property.
      data: data,
    },
  });
};

//报告融合-结论建议
const drawConclusionChart = function(data) {
  const option = {
    title: {
      text: "结论建议词云",
      left: "center",
      top: "bottom",
      textStyle: {
        fontWeight: 400,
        fontSize: "14px",
      },
    },
    series: {
      type: "wordCloud",
      shape: "cardioid",
      keepAspect: false,

      left: "center",
      top: "center",
      width: "70%",
      height: "80%",
      right: null,
      bottom: null,
      sizeRange: [12, 60],
      rotationRange: [-90, 90],
      rotationStep: 45,
      gridSize: 8,
      drawOutOfBound: false,
      layoutAnimation: true,
      textStyle: {
        fontFamily: "sans-serif",
        fontWeight: "bold",
        // Color can be a callback function or a color string
        color: function() {
          // Random color
          return "rgb(" + [
            Math.round(Math.random() * 160),
            Math.round(Math.random() * 160),
            Math.round(Math.random() * 160),
          ].join(",") + ")";
        },
      },
      emphasis: {
        focus: "self",
        textStyle: {
          textShadowBlur: 10,
          textShadowColor: "#C0C0D0",
        },
      },
      data: data,
    },
  };
  conclusionChart.setOption(option);
};

//饼状图
//测试环境
const drawEnvironmentChart = function(envData) {
  const data = envData.sort((envData1, envData2) => {
    return envData1.value - envData2.value;
  });
  const option = {
    title: {
      text: "测试环境统计图",
      left: "center",
      top: "bottom",
      textStyle: {
        fontWeight: 400,
        fontSize: "14px",
      },
    },
    series: [
      {
        label: {
          position: "inside",
        },
        name: "Nightingale Chart",
        type: "pie",
        radius: [20, 120],
        roseType: "area",
        itemStyle: {
          borderRadius: 4,
        },
        data: data,
      },
    ],
  };
  environmentChart.setOption(option);
};

//测试工具
const drawToolChart = function(toolData) {
  const data = toolData.sort((tool1, tool2) => {
    return tool1.value - tool2.value;
  });
  const option = {
    title: {
      text: "测试工具统计图",
      left: "center",
      top: "bottom",
      textStyle: {
        fontWeight: 400,
        fontSize: "14px",
      },
    },
    series: [
      {
        name: "Nightingale Chart",
        type: "pie",
        radius: [20, 120],
        roseType: "area",
        itemStyle: {
          borderRadius: 4,
        },
        label: {
          position: "inside",
        },
        data: data,
      },
    ],
  };
  toolChart.setOption(option);
};


//用例统计
const drawCaseChart = function(caseData) {
  const data = caseData;
  data.forEach((item) => {
    item.caseName = `用例${item.caseId}`;
  });
  const option = {
    dataset: {
      source: data,
    },
    tooltip: {
      trigger: "axis",
      axisPointer: {
        type: "shadow",
      },
    },
    legend: {
      left: "center",
      top: "top",
    },
    grid: {
      left: "3%",
      right: "4%",
      bottom: "3%",
      containLabel: true,
    },
    yAxis: {
      type: "value",
    },
    xAxis: {
      type: "category",
    },
    series: [
      {
        encode: {
          x: "caseName",
          y: "pass",
        },
        name: "pass",
        type: "bar",
        stack: "total",
        label: {
          show: true,
        },
        itemStyle: {
          color: "#5A6FC0",
        },
        emphasis: {
          focus: "series",
        },
      },
      {
        encode: {
          x: "caseName",
          y: "failed",
        },
        name: "failed",
        type: "bar",
        stack: "total",
        itemStyle: {
          color: "#F2CA6B",
        },
        label: {
          show: true,
        },
        emphasis: {
          focus: "series",
        },
      },
    ],
  };
  caseChart.setOption(option);
};


//报告融合--关系图相关
const drawRelationChart = function(relationData) {
  const data = relationData;
  const option = {
    title: {
      text: "报告关系图",
      left: "center",
      top: "bottom",
      textStyle: {
        fontWeight: 400,
        fontSize: "14px",
      },
    },
    tooltip: {},
    animationDuration: 1500,
    animationEasingUpdate: "quinticInOut",
    legend: [
      {
        data: data.categories.map(function(a) {
          return a.name;
        }),
      },
    ],
    series: {
      type: "graph",
      layout: "none",
      data: data.nodes,
      links: data.links,
      categories: data.categories,
      lineStyle: {
        color: "source",
        curveness: 0.4,
        width: 2,
      },
      label: {
        show: true,
        position: "top",
        formatter: "{b}",
      },
      emphasis: {
        focus: "adjacency",
        lineStyle: {
          width: 10,
        },
      },
    },
  };
  relationChart.setOption(option);
  relationChart.on("click", function(params) {
    const { data } = params;
    if (data.category === 2) {
      const { value } = params.data;
      toReportDetailPage(value);
    }
  });
};
</script>

<style lang="scss" scoped>
@import "style/reportSummary";

</style>
