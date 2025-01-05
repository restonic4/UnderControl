package com.chaotic_loom.under_control.util;

import com.chaotic_loom.under_control.UnderControl;
import com.chaotic_loom.under_control.api.server.ServerAPI;

import java.util.List;

public class ServerData {
    private boolean online;
    private String host;
    private int port;
    private String ipAddress;
    private boolean eulaBlocked;
    private long retrievedAt;
    private long expiresAt;
    private Object srvRecord;
    private Version version;
    private Players players;
    private Motd motd;
    private String icon;
    private List<String> mods;
    private String software;
    private List<String> plugins;

    public void update() {
        ServerData newServerData = ServerAPI.getServerData(getHost() + ":" + getPort());

        if (newServerData == null) {
            UnderControl.LOGGER.warn("Could not update server data");
            return;
        }

        setOnline(newServerData.isOnline());
        setHost(newServerData.getHost());
        setPort(newServerData.getPort());
        setIpAddress(newServerData.getIpAddress());
        setEulaBlocked(newServerData.isEulaBlocked());
        setRetrievedAt(newServerData.getRetrievedAt());
        setExpiresAt(newServerData.getExpiresAt());
        setSrvRecord(newServerData.getSrvRecord());
        setIcon(newServerData.getIcon());
        setMods(newServerData.getMods());
        setSoftware(newServerData.getSoftware());
        setPlugins(newServerData.getPlugins());
        setVersion(newServerData.getVersion());
        setPlayers(newServerData.getPlayers());
        setMotd(newServerData.getMotd());
    }

    public boolean isOnline() {
        return online;
    }

    public void setOnline(boolean online) {
        this.online = online;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public boolean isEulaBlocked() {
        return eulaBlocked;
    }

    public void setEulaBlocked(boolean eulaBlocked) {
        this.eulaBlocked = eulaBlocked;
    }

    public long getRetrievedAt() {
        return retrievedAt;
    }

    public void setRetrievedAt(long retrievedAt) {
        this.retrievedAt = retrievedAt;
    }

    public long getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(long expiresAt) {
        this.expiresAt = expiresAt;
    }

    public Object getSrvRecord() {
        return srvRecord;
    }

    public void setSrvRecord(Object srvRecord) {
        this.srvRecord = srvRecord;
    }

    public Version getVersion() {
        return version;
    }

    public void setVersion(Version version) {
        this.version = version;
    }

    public Players getPlayers() {
        return players;
    }

    public void setPlayers(Players players) {
        this.players = players;
    }

    public Motd getMotd() {
        return motd;
    }

    public void setMotd(Motd motd) {
        this.motd = motd;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public List<String> getMods() {
        return mods;
    }

    public void setMods(List<String> mods) {
        this.mods = mods;
    }

    public String getSoftware() {
        return software;
    }

    public void setSoftware(String software) {
        this.software = software;
    }

    public List<String> getPlugins() {
        return plugins;
    }

    public void setPlugins(List<String> plugins) {
        this.plugins = plugins;
    }

    public static class Version {
        private String name_raw;
        private String name_clean;
        private String name_html;
        private int protocol;

        public String getName_raw() {
            return name_raw;
        }

        public void setName_raw(String name_raw) {
            this.name_raw = name_raw;
        }

        public String getName_clean() {
            return name_clean;
        }

        public void setName_clean(String name_clean) {
            this.name_clean = name_clean;
        }

        public String getName_html() {
            return name_html;
        }

        public void setName_html(String name_html) {
            this.name_html = name_html;
        }

        public int getProtocol() {
            return protocol;
        }

        public void setProtocol(int protocol) {
            this.protocol = protocol;
        }
    }

    public static class Motd {
        private String raw;
        private String clean;
        private String html;

        public String getRaw() {
            return raw;
        }

        public void setRaw(String raw) {
            this.raw = raw;
        }

        public String getClean() {
            return clean;
        }

        public void setClean(String clean) {
            this.clean = clean;
        }

        public String getHtml() {
            return html;
        }

        public void setHtml(String html) {
            this.html = html;
        }
    }

    public static class Players {
        private int online;
        private int max;
        private List<String> list;

        public int getOnline() {
            return online;
        }

        public void setOnline(int online) {
            this.online = online;
        }

        public int getMax() {
            return max;
        }

        public void setMax(int max) {
            this.max = max;
        }

        public List<String> getList() {
            return list;
        }

        public void setList(List<String> list) {
            this.list = list;
        }
    }
}
