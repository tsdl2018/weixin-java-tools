package me.chanjar.weixin.mp.api.impl;

import com.google.inject.Inject;
import me.chanjar.weixin.common.exception.WxErrorException;
import me.chanjar.weixin.mp.api.ApiTestModule;
import me.chanjar.weixin.mp.api.WxXmlMpInMemoryConfigStorage;
import me.chanjar.weixin.mp.bean.WxMpGroup;
import org.testng.Assert;
import org.testng.annotations.Guice;
import org.testng.annotations.Test;

import java.util.List;

/**
 * 测试分组接口
 *
 * @author chanjarster
 */
@Deprecated
@Test(groups = "groupAPI")
@Guice(modules = ApiTestModule.class)
public class WxMpGroupServiceImplTest {

  @Inject
  protected WxMpServiceImpl wxService;

  protected WxMpGroup group;

  public void testGroupCreate() throws WxErrorException {
    WxMpGroup res = this.wxService.getGroupService().groupCreate("测试分组1");
    Assert.assertEquals(res.getName(), "测试分组1");
  }

  @Test(dependsOnMethods="testGroupCreate")
  public void testGroupGet() throws WxErrorException {
    List<WxMpGroup> groupList = this.wxService.getGroupService().groupGet();
    Assert.assertNotNull(groupList);
    Assert.assertTrue(groupList.size() > 0);
    for (WxMpGroup g : groupList) {
      this.group = g;
      Assert.assertNotNull(g.getName());
    }
  }

  @Test(dependsOnMethods={"testGroupGet", "testGroupCreate"})
  public void getGroupUpdate() throws WxErrorException {
    this.group.setName("分组改名");
    this.wxService.getGroupService().groupUpdate(this.group);
  }

  public void testGroupQueryUserGroup() throws WxErrorException {
    WxXmlMpInMemoryConfigStorage configStorage = (WxXmlMpInMemoryConfigStorage) this.wxService.getWxMpConfigStorage();
    long groupid = this.wxService.getGroupService().userGetGroup(configStorage.getOpenid());
    Assert.assertTrue(groupid != -1l);
  }

  public void testGroupMoveUser() throws WxErrorException {
    WxXmlMpInMemoryConfigStorage configStorage = (WxXmlMpInMemoryConfigStorage) this.wxService.getWxMpConfigStorage();
    this.wxService.getGroupService().userUpdateGroup(configStorage.getOpenid(), this.wxService.getGroupService().groupGet().get(3).getId());
  }

}