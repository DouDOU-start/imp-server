# 影像管理平台服务端

> 作者：Allen Huang
> 
> 邮箱：allen.huang@hanglok-tech.cn
> 
> 项目背景：致力于对医学影像数据进行存档和管理，包括影像文件、标注文件等。

## v1.0.0 功能架构设计

### v1.0.0 预计周期（三个月） 起始日期：2023.05.18 完成日期：

- 系统架构建设：
  - [x] 影像管理平台需求分析；
  - [x] 影像管理平台原型设计；
  - [x] 后台服务技术架构选型，Java17+SpringBoot3+MyBatis-Plus+MySQL+Redis;
  - [x] 影像管理平台数据库设计；
  - [x] Springdoc-OpenApi，Knife4j在线接口文档服务搭建；

- Dicom模块：
  - [x] 分页获取系列简要信息列表，附条件过滤，*除性别和切片厚度外其他条件可多选*
    - [x] 模糊关键字
    - [x] 机构
    - [x] 模态
    - [x] 切片厚度（范围）
    - [x] 身体检查部位
    - [x] 性别
    - [ ] 器官
    - [x] 扫描类型（平扫，增强，... ）
  - [ ] 获取系列数据详情：
    - 模态 modality
    - 形状信息 shapeInfo：row * columns * slicesNum，例如：512 * 512 * N；
    - 切片信息 sliceInfo pixelSpacing * sliceThickness，例如：0.7 * 0.7 * 3.00；
    - 身体检查部位 body_part，例如：胸部、肺部、etc.；
    - 标注文件信息 labelInfo：{ fileLocation, fileStorageType ... }
    - 实例信息 instanceInfo：{ UID, sliceLocation, createAt ... }
  - [ ] 标签数据转换，对上传标签文件进行数据转换，由军哥提供能力服务；


- 维度信息模块：
  - [x] 获取机构信息列表；
  - [x] 获取模态信息列表；
  - [ ] 扫描类型管理，新增、修改、深处，获取扫描类型列表（平扫、增强，...） 
  - [ ] 器官信息管理，新增、修改、删除，获取器官信息列表；
  - [ ] 身体检查部位管理，新增、修改、删除，获取身体检查部位列表；

- 文件模块：

  > 文件存储目录树：
  > ```
  > | |——dicom
  > | | |——institution_name
  > | | | |——patient_id
  > | | | | |——studies_uid
  > | | | | | |——series_uid
  > | | | | | | |——instance_number.dcm
  > | |——label
  > | | |——series_id.nii or series_id.nii.gz
  > | |——labelBak
  > | | |——series_id.nii or series_id.nii.gz
  > ```

  - [x] 上传DICOM文件，数据入库；
  - [ ] 下载DICOM文件；
  - [ ] 上传标注文件(nii格式)；
  - [ ] 下载标注文件；
  - 下载影像文件包:
    - [ ] 单个影像包格式，影像包zip -> [ DICOM文件夹 -> [ DICOM1, DICOM2, ... ] ，标注文件 ]
    - [ ] 批量影像包格式，影像包.zip -> [ DICOM文件夹 -> [ series1 -> [ DICOM1, DICOM2, ... ], series2, ... ]，标注文件夹 -> [ nii1, nii2, ... ] ]
  - 标签文件回收站
    - [ ] 定时清理回收站标签文件
    - [ ] 回收站标签文件恢复

- 工具模块：
  - [x] 校验DICOM文件、解析DICOM文件、修复DICOM文件中文乱码
  - [x] OkHttp请求工具类
  - [x] 对象转换，实体类转DTO、DTO转实体类
  - [x] 组合条件校验器

### 功能实现进度

| 功能名称         | 进度    | 完成时间     |
|--------------|-------|----------|
| 分页获取系列简要信息列表 | 87.5% |          |
| 获取系列数据详情     |       |          |
| 标签数据转换       |       |          |
| 获取机构信息列表     | 100%  | 20230612 |
| 获取模态信息列表     | 100%  | 20230612 |
| 扫描类型管理       |       |          |
| 器官信息管理       |       |          |
| 身体检查部位管理     |       |          |
| 上传DICOM文件    | 100%  | 20230609 |
| 下载DICOM文件    |       |          |
| 下载标注文件       |       |          |
| 下载影像文件包      |       |          |