package club.c1sec.c1ctfplatform.services;

import club.c1sec.c1ctfplatform.dao.ConfigDao;
import club.c1sec.c1ctfplatform.po.Config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.time.Instant;
import java.util.Date;

@Service
@DependsOn({"configDao"})
public class ConfigService {
    @Autowired
    ConfigDao configDao;

    private final String dynamicScoreBaseKey = "dynamic_score_base";
    private int dynamicScoreBase;

    private final String dynamicScoreMinKey = "dynamic_score_min";
    private int dynamicScoreMin;

    private final String matchOpenTimeKey = "match_open_time";
    private Instant matchOpenTime;

    private final String matchEndTimeKey = "match_end_time";
    private Instant matchEndTime;

    private final String registerOpenKey = "register_open";
    private boolean registerOpen;

    private final String containerCountKey = "container_count";
    private int containerCount;

    private final String containerFlagFormatKey = "container_flag_format";
    private String containerFlagFormat;

    private final String loginLimitKey = "login_limit";
    private boolean loginLimit;

    @PostConstruct
    public void initConfigService() {
        this.refreshConfig();
    }

    public int getDynamicScoreBase() {
        return this.dynamicScoreBase;
    }

    public int getDynamicScoreMin() {
        return this.dynamicScoreMin;
    }

    public Instant getMatchOpenTime() {
        return this.matchOpenTime;
    }

    public Instant getMatchEndTime() {
        return this.matchEndTime;
    }

    public boolean getRegisterOpen() {
        return this.registerOpen;
    }

    public int getContainerCount() {
        return this.containerCount;
    }

    public String getContainerFlagFormat() {
        return this.containerFlagFormat;
    }

    public boolean getLoginLimit() {
        return this.loginLimit;
    }

    public boolean getMatchStarted() {
        Date currDate = new Date();
        Date openDate = Date.from(getMatchOpenTime());
        return currDate.after(openDate);
    }

    public boolean getMatchOpen() {
        Date currDate = new Date();
        Date openDate = Date.from(getMatchOpenTime());
        Date endDate = Date.from(getMatchEndTime());
        return currDate.before(endDate) && currDate.after(openDate);
    }

    public void setDynamicScoreBase(int value) {
        Config config = new Config();
        config.setKey(dynamicScoreBaseKey);
        config.setValue(Integer.toString(value));
        configDao.save(config);
    }

    public void setDynamicScoreMin(int value) {
        Config config = new Config();
        config.setKey(dynamicScoreMinKey);
        config.setValue(Integer.toString(value));
        configDao.save(config);
    }

    public void setMatchOpenTime(Instant value) {
        Config config = new Config();
        config.setKey(matchOpenTimeKey);
        config.setValue(value.toString());
        configDao.save(config);
    }

    public void setMatchEndTime(Instant value) {
        Config config = new Config();
        config.setKey(matchEndTimeKey);
        config.setValue(value.toString());
        configDao.save(config);
    }

    public void setContainerCount(int value) {
        Config config = new Config();
        config.setKey(containerCountKey);
        config.setValue(Integer.toString(value));
        configDao.save(config);
    }

    public void setContainerFlagFormat(String value) {
        Config config = new Config();
        config.setKey(containerFlagFormatKey);
        config.setValue(value);
        configDao.save(config);
    }

    public void setRegisterOpen(boolean value) {
        Config config = new Config();
        config.setKey(registerOpenKey);
        config.setValue(Boolean.toString(value));
        configDao.save(config);
    }

    public void setLoginLimit(boolean value) {
        Config config = new Config();
        config.setKey(loginLimitKey);
        config.setValue(Boolean.toString(value));
        configDao.save(config);
    }

    public void refreshDynamicScoreBase() {
        try {
            Config config = configDao.findConfigByKey(dynamicScoreBaseKey);
            this.dynamicScoreBase = Integer.parseInt(config.getValue());
        } catch (Exception e) {
            this.dynamicScoreBase = 1000;
        }
    }

    public void refreshDynamicScoreMin() {
        try {
            Config config = configDao.findConfigByKey(dynamicScoreMinKey);
            this.dynamicScoreMin = Integer.parseInt(config.getValue());
        } catch (Exception e) {
            this.dynamicScoreMin = 10;
        }
    }

    public void refreshMatchOpenTime() {
        try {
            Config config = configDao.findConfigByKey(matchOpenTimeKey);
            this.matchOpenTime = Instant.parse(config.getValue());
        } catch (Exception e) {
            this.matchOpenTime = Instant.parse("2019-10-27T07:44:00Z");
        }
    }

    public void refreshMatchEndTime() {
        try {
            Config config = configDao.findConfigByKey(matchEndTimeKey);
            this.matchEndTime = Instant.parse(config.getValue());
        } catch (Exception e) {
            this.matchEndTime = Instant.parse("2019-11-27T07:45:00Z");
        }
    }


    public void refreshRegisterOpen() {
        try {
            Config config = configDao.findConfigByKey(registerOpenKey);
            this.registerOpen = Boolean.parseBoolean(config.getValue());
        } catch (Exception e) {
            this.registerOpen = true;
        }
    }

    public void refreshContainerCount() {
        try {
            Config config = configDao.findConfigByKey(containerCountKey);
            this.containerCount = Integer.parseInt(config.getValue());
        } catch (Exception e) {
            this.containerCount = 0;
        }
    }

    public void refreshContainerFlagFormat() {
        try {
            Config config = configDao.findConfigByKey(containerFlagFormatKey);
            this.containerFlagFormat = config.getValue();
        } catch (Exception e) {
            this.containerFlagFormat = "c1ctf{}";
        }
    }

    public void refreshLoginLimit() {
        try {
            Config config = configDao.findConfigByKey(loginLimitKey);
            this.loginLimit = Boolean.parseBoolean(config.getValue());
        } catch (Exception e) {
            this.loginLimit = false;
        }
    }

    public void refreshConfig() {
        this.refreshDynamicScoreBase();
        this.refreshDynamicScoreMin();
        this.refreshMatchOpenTime();
        this.refreshMatchEndTime();
        this.refreshRegisterOpen();
        this.refreshContainerCount();
        this.refreshContainerFlagFormat();
        this.refreshLoginLimit();
    }
}
