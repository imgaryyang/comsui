package com.zufangbao.earth.web.controller.tag;

import com.alibaba.fastjson.serializer.SerializerFeature;
import com.demo2do.core.utils.JsonUtils;
import com.demo2do.core.web.interceptor.MenuSetting;
import com.demo2do.core.web.resolver.Page;
import com.demo2do.core.web.resolver.Secure;
import com.yiji.openapi.tool.fastjson.JSON;
import com.zufangbao.earth.util.DownloadUtils;
import com.zufangbao.earth.web.controller.BaseController;
import com.zufangbao.earth.yunxin.handler.SystemOperateLogHandler;
import com.zufangbao.gluon.util.IpUtil;
import com.zufangbao.gluon.util.SpringContextUtil;
import com.zufangbao.sun.entity.security.Principal;
import com.zufangbao.sun.entity.tag.*;
import com.zufangbao.sun.handler.impl.tag.TagOpsHandlerImpl;
import com.zufangbao.sun.handler.tag.TagHandler;
import com.zufangbao.sun.handler.tag.TagOpsHandler;
import com.zufangbao.sun.service.tag.ITagWrapper;
import com.zufangbao.sun.service.tag.TagConfigService;
import com.zufangbao.sun.service.tag.TagService;
import com.zufangbao.sun.yunxin.entity.model.tag.TagQueryModel;
import com.zufangbao.sun.yunxin.log.LogFunctionType;
import com.zufangbao.sun.yunxin.log.LogOperateType;
import com.zufangbao.sun.yunxin.log.SystemOperateLogRequestParam;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

@RestController
@RequestMapping("/tag")
@MenuSetting("menu-tag")
public class TagController extends BaseController {

	@Autowired
	private TagHandler tagHandler;

	@Autowired
	private TagService tagService;

	@Autowired
	private TagConfigService tagConfigService;

	@Autowired
	private TagOpsHandler tagOpsHandler;

	@Autowired
	private SystemOperateLogHandler systemOperateLogHandler;



    private static String localFilePath;

    @Value("#{config['uploadPath']}")
    private void setSavePath(String uploadPath){
        if(StringUtils.isEmpty(uploadPath)){
            localFilePath = getClass().getResource(".").getFile() + "tag/" ;
        }else if(uploadPath.endsWith(File.separator)){
            localFilePath = uploadPath+ "tag"+ File.separator;
        }else{
            localFilePath =  uploadPath+ File.separator+ "tag"+ File.separator;
        }
    }

	private static final Log logger = LogFactory.getLog(TagController.class);

	// 查询标签列表页
	@RequestMapping("/query")
	@MenuSetting("submenu-tag")
	public @ResponseBody String query(@ModelAttribute TagQueryModel tagQueryModel,Page page) {
		try {
			int size = tagService.count(tagQueryModel);
			List<Tag> tags = tagService.query(tagQueryModel, page);
			Map<String, Object> data = new HashMap<>();
			data.put("list", tags);
			data.put("size", size);
			return jsonViewResolver.sucJsonResult(data, SerializerFeature.DisableCircularReferenceDetect);
		} catch (Exception e) {
			logger.error("##TagController-query## error!!");
			e.printStackTrace();
			return jsonViewResolver.errorJsonResult(QUERY_ERROR);
		}
	}

	// 标签数据获取
	@RequestMapping(value = "/detail", method = RequestMethod.GET)
	@MenuSetting("submenu-tag")
	public @ResponseBody String detail(@RequestParam(value = "uuid") String uuid) {
		try {
			Tag tag = tagService.getTagByUuidOrName(uuid, null);
			Map<String, Object> data = new HashMap<>();
			data.put("tag", tag);
			return jsonViewResolver.sucJsonResult(data, SerializerFeature.DisableCircularReferenceDetect);
		} catch (Exception e) {
			logger.error("##TagController-detail## error!!");
			e.printStackTrace();
			return jsonViewResolver.errorJsonResult("标签数据获取失败！！！");
		}
	}

	// 标签配置信息获取
	@RequestMapping(value = "/detail/query", method = RequestMethod.GET)
	@MenuSetting("submenu-tag")
	public @ResponseBody String getTagConfigData(@RequestParam(value = "uuid") String uuid, Page page) {
		try {
			Tag tag = tagService.getTagByUuidOrName(uuid, null);
			if(tag == null){
				return jsonViewResolver.errorJsonResult("标签数据获取失败！！！");
			}
			int cnt =tagConfigService.countTagConfigByTagUuid(uuid);
			List<TagIdentityMap> tagIdentityMaps = tagConfigService.getTagConfigByTagUuid(uuid, page);
			Map<String, Object> data = new HashMap<>();
			data.put("size", cnt);
			data.put("list", tagIdentityMaps);
			return jsonViewResolver.sucJsonResult(data, SerializerFeature.DisableCircularReferenceDetect);
		} catch (Exception e) {
			logger.error("##TagController-detail## error!!");
			e.printStackTrace();
			return jsonViewResolver.errorJsonResult("标签数据获取失败！！！");
		}
	}


	// 编辑-标签数据提交
	@RequestMapping(value = "/edit-tag", method = RequestMethod.POST)
	@MenuSetting("submenu-tag")
	public @ResponseBody String editTag(
			@RequestParam(value = "uuid") String uuid,
			@RequestParam(value = "name") String name,
			@RequestParam(value = "description")  String description,
			@RequestParam(value = "deleteUuids",required = false)  String deleteUuidJson,
			@Secure Principal principal, HttpServletRequest request) {
		try {
			if (StringUtils.isEmpty(name)) {
				return jsonViewResolver.errorJsonResult("请输入标签名称！！");
			}
			boolean exist = tagService.existTagName(name,uuid);
			if (exist) {
				return jsonViewResolver.errorJsonResult("标签名称已存在！！");
			}
			List<String> deleteUuids = JsonUtils.parseArray(deleteUuidJson, String.class);
            boolean isOk = tagHandler.modifyTag(uuid, name, description, deleteUuids, principal.getId(), IpUtil.getIpAddress(request));

			return jsonViewResolver.jsonResult(isOk);
		}catch (Exception e) {
			logger.error("##TagController-editTag## error!!");
			e.printStackTrace();
			return jsonViewResolver.errorJsonResult("编辑标签失败！！！");
		}

	}

	// 新增标签
	@RequestMapping(value = "/create-tag", method = RequestMethod.POST)
	@MenuSetting("submenu-tag")
	public @ResponseBody String createTag(
			@RequestParam(value = "name") String name,
			@RequestParam(value = "description") String description,
			@Secure Principal principal, HttpServletRequest request) {
		try {
			if (StringUtils.isEmpty(name)) {
				return jsonViewResolver.errorJsonResult("请输入标签名称！！");
			}
			boolean exist = tagService.existTagName(name,null);
			if (exist) {
				return jsonViewResolver.errorJsonResult("标签名称已存在！！");
			}
			Tag tag = tagService.createTag(name, description);
			SystemOperateLogRequestParam param = getSystemOperateLogrequestParam(principal, request, tag);
			systemOperateLogHandler.generateSystemOperateLog(param);
			return jsonViewResolver.sucJsonResult();
		}catch (Exception e) {
			logger.error("##TagController-createTag## error!!");
			e.printStackTrace();
			return jsonViewResolver.errorJsonResult("新增标签失败！！！");
		}

	}

	private SystemOperateLogRequestParam getSystemOperateLogrequestParam(Principal principal,HttpServletRequest request, Tag tag) {
		return new SystemOperateLogRequestParam(
                principal.getId(), IpUtil.getIpAddress(request),
                tag.getName(), LogFunctionType.ADDTAG, LogOperateType.ADD,
				Tag.class, tag, null, null);
	}


	// 删除
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	@MenuSetting("submenu-tag")
	public @ResponseBody String delete(@RequestParam(value = "uuid") String uuid) {
		try {
			boolean result = tagHandler.deleteTag(uuid);
			if (result) {
				return jsonViewResolver.sucJsonResult();
			}
			return jsonViewResolver.errorJsonResult("标签删除失败！！！");
		} catch (Exception e) {
			logger.error("##TagController-delete## error!!");
			e.printStackTrace();
			return jsonViewResolver.errorJsonResult("标签删除失败！！！");
		}
	}



	// 查询所有标签信息
	@RequestMapping("/names")
	@MenuSetting("submenu-tag")
	public @ResponseBody String getAllTags() {
		try {
			List<TagInfoModel> allTagInfo = tagService.getAllTagInfo();
			return jsonViewResolver.sucJsonResult("data", allTagInfo, SerializerFeature.DisableCircularReferenceDetect);
		} catch (Exception e) {
			logger.error("##TagController-query## error!!");
			e.printStackTrace();
			return jsonViewResolver.errorJsonResult(QUERY_ERROR);
		}
	}

	// 显示某个单据的标签
	@RequestMapping(value = "/show", method = RequestMethod.POST)
	public @ResponseBody String showTag(@RequestParam(value = "no") String identifier){
		try{
			List<TagConfigVO> results =  tagOpsHandler.getTagInfosByOuterIdentifier(identifier, null);
			return jsonViewResolver.sucJsonResult("data",results, SerializerFeature.DisableCircularReferenceDetect);
		} catch (Exception e){
			logger.error("##TagController-showTag## error!!");
			e.printStackTrace();
			return jsonViewResolver.errorJsonResult("获取标签失败");
		}
	}

	// 删除单据的标签
	@RequestMapping(value = "/delcfg", method = RequestMethod.POST)
	public @ResponseBody String delTagConfig(@RequestParam(value = "uuid") String tagConfigUuid){
		try{
			tagConfigService.deleteATagConfig(tagConfigUuid);
			return jsonViewResolver.sucJsonResult();
		} catch (Exception e){
			logger.error("##TagController-delTagConfig## error!!");
			e.printStackTrace();
			return jsonViewResolver.errorJsonResult("删除标签失败");
		}
	}


	// 给单条单据添加标签
	@RequestMapping(value = "/add/single", method = RequestMethod.POST)
	public @ResponseBody String addTagSingle(
		@RequestParam(value = "no") String identifier,
		@RequestParam(value = "type") Integer type,
		@RequestParam(value = "defaultTagList") String defaultTagListJsonStr,
		@RequestParam(value = "transitivityTagList", required = false) String transitivityTagListJsonStr){
		try{
			TagConfigType typeEnum = TagConfigType.fromCode(type);
			if(typeEnum == null){
				return jsonViewResolver.errorJsonResult("数据错误");
			}
			List<String> defaultTagList = JSON.parseArray(defaultTagListJsonStr, String.class);
			List<String> transitivityTagList = JSON.parseArray(transitivityTagListJsonStr, String.class);
			List<TagConfigVO> results = new ArrayList<>();

			List<TagConfigVO> defaultTagResults = tagOpsHandler.postSingleItemWithMultiTags(identifier, type, defaultTagList, false);
			results.addAll(defaultTagResults);

			List<TagConfigVO> transitivityTagResults = tagOpsHandler.postSingleItemWithMultiTags(identifier, type, transitivityTagList, true);
			results.addAll(transitivityTagResults);

			return jsonViewResolver.sucJsonResult("data",results,SerializerFeature.DisableCircularReferenceDetect);
		} catch (Exception e){
			logger.error("##TagController-addTagSingle## error!!");
			e.printStackTrace();
			return jsonViewResolver.errorJsonResult("新增标签失败");
		}
	}

	// 上传文件 解析单号
    @RequestMapping(value = "/add/up", method = RequestMethod.POST)
    public @ResponseBody
    String checkNosFromFile(
        @RequestParam(value = "type") Integer type, HttpServletRequest request) {
        try {
            MultipartHttpServletRequest fileRequest = (MultipartHttpServletRequest) request;
            Iterator<String> fileNames = fileRequest.getFileNames();
            String fileName = fileNames.next();
            MultipartFile multipartFile = fileRequest.getFile(fileName);
			String originFilename = multipartFile.getOriginalFilename(); // 获得原始文件名  c:\a\b\1.txt OR 1.txt
			String filename = originFilename.substring(originFilename.lastIndexOf(File.separator) + 1);// 获得文件名1.txt
			if(!checkFilename(filename)){
				return jsonViewResolver.errorJsonResult("仅支持txt格式,以换行符隔开数据");
			}
			if(TagConsts.FILE_MAX_SIZE < multipartFile.getSize()){
				return jsonViewResolver.errorJsonResult("文件过大");
			}
            Map<String, Object> result = tagOpsHandler.parseFileAndGenerateResult(multipartFile, type, localFilePath);
            return jsonViewResolver.sucJsonResult(result, SerializerFeature.DisableCircularReferenceDetect);
		} catch (Exception e) {
			logger.error("##TagController-checkNosFromFile## error!!");
            e.printStackTrace();
            return jsonViewResolver.errorJsonResult("文件校验失败,"+e.getMessage());
        }
    }

	// 下载结果文件
	@RequestMapping(value = "/add/download", method = RequestMethod.GET)
	public @ResponseBody
	void downloadResultFile(
		@RequestParam(value = "fileKey") String fileKey, HttpServletResponse response) {
		try {
			File file = new File(
				localFilePath + fileKey + TagOpsHandlerImpl.SUFFIX_RESULT_FILE + TagOpsHandlerImpl.SUFFIX_CSV);
			if (!file.exists()) {
				logger.error("Downloading file error. FilePath: " + file.getPath() + fileKey);
				return;
			}
			//处理文件名
			String realName = "校验结果.csv";
			DownloadUtils.flushFileIntoHttp(realName, "", file.getAbsolutePath(), response);
		} catch (Exception e) {
			logger.error("##TagController-downloadResultFile## error!!");
			e.printStackTrace();
		}
	}

	// 提交结果 从文件 actionType 提交:add 删除:delete
	@RequestMapping(value = "/submit/1", method = RequestMethod.POST)
	public @ResponseBody String submitResult(
		@RequestParam(value = "fileKey") String fileKey,
		@RequestParam(value = "type") Integer type,
		@RequestParam(value = "defaultTagList") String defaultTagListJsonStr,
		@RequestParam(value = "transitivityTagList", required = false) String transitivityTagListJsonStr,
	    @RequestParam(value = "actionType") String actionType) {
		try {
			File file = new File(
				localFilePath + fileKey + TagOpsHandlerImpl.SUFFIX_GOOD_CONTENT);
			List<String> defaultTagList = JSON.parseArray(defaultTagListJsonStr, String.class);
			List<String> transitivityTagList = JSON.parseArray(transitivityTagListJsonStr, String.class);
			if("add".equals(actionType)){
				tagOpsHandler.postTagFromFile(file, type, defaultTagList, transitivityTagList);
			}
			if("delete".equals(actionType)){
				tagOpsHandler.deleteTagFromFile(file, type, defaultTagList, transitivityTagList);
			}
			return jsonViewResolver.sucJsonResult();
		} catch (Exception e) {
			logger.error("##TagController-submitResult## error!!");
			e.printStackTrace();
			return jsonViewResolver.errorJsonResult(e.getMessage());
		}
	}

	@RequestMapping(value = "/submit/2", method = RequestMethod.POST)
	public @ResponseBody
	String submitResult2(@RequestParam(value = "type") Integer type,
						 @RequestParam(value = "actionType") String actionType,
						 @RequestParam(value = "defaultTagList") String defaultTagListJsonStr,
						 @RequestParam(value = "transitivityTagList", required = false) String transitivityTagListJsonStr,
						 HttpServletRequest request) {
		try {
			List<String> defaultTagList = JSON.parseArray(defaultTagListJsonStr, String.class);
			List<String> transitivityTagList = JSON.parseArray(transitivityTagListJsonStr, String.class);
			Class<?> paramsClass = TagConsts.TAG_TYPE_PARAMS_CLASS_MAPPER.get(type);
			if(paramsClass == null){
				return jsonViewResolver.errorJsonResult("不支持的数据类型");
			}
			// 根据单据类型，获取包装类
			Class<? extends ITagWrapper<?>> wrapperClass = TagConsts.TAG_TYPE_PARAMS_BEAN_MAPPER.get(type);

			// 将查询参数封装至bean
			Object paramsBean = paramsClass.newInstance();
			BeanUtils.populate(paramsBean, request.getParameterMap());
			ITagWrapper wrapper = SpringContextUtil.getBean(wrapperClass);

			wrapper.handler(paramsBean, type, defaultTagList,transitivityTagList, actionType);

		} catch (IllegalAccessException | InstantiationException | InvocationTargetException e) {
			e.printStackTrace();
			return jsonViewResolver.errorJsonResult(e.getMessage());
		} catch (RuntimeException e) {
			e.printStackTrace();
			return jsonViewResolver.errorJsonResult(e.getMessage());
		}
		return jsonViewResolver.sucJsonResult();
	}

	private boolean checkFilename(String filename){
		List<String> valaidFileformat = Arrays.asList("txt");
		//获得扩展名
		int dotFlag = filename.lastIndexOf(".");
		String fileExtName = dotFlag == -1 ? "" : filename.substring(dotFlag + 1).toLowerCase();
		return valaidFileformat.contains(fileExtName);
	}


}
