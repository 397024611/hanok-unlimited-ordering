# Hanok Wagga — Long-Term Project Memory / Single Source of Truth (SSOT)

> **Project:** Wagga Hanok 重启计划  
> **Store / Scope:** Hanok Korean BBQ — Wagga Wagga  
> **Purpose:** This is the canonical long-term memory file for **Hanok Wagga only**.  
> **Last updated:** 2026-10-04 13:27 AEDT  
> **Status:** ACTIVE / CANONICAL  

---

## 0. 核心原则：绝对不要混淆

这份文件只记录 **Hanok Wagga / Wagga 重启项目**。

### Wagga 当前正确项目
- 产品/系统定位：**Hanok Wagga Unlimited Ordering / 点单式自助**
- 当前 GitHub：`397024611/hanok-unlimited-ordering`
- 当前 Vercel：`hanok-unlimited-ordering-za6y`
- 当前正式域名：`orderhanokbbqwagga.com`
- 当前 Supabase：`hanokwagga buffet`
- Supabase project ref：`pycuztuyrhdkqlepbinh`

### 明确禁止混用
以下内容**不是 Wagga 当前 canonical 项目**：
- `meathouse-skewer-ordering`
- `meathouseskewer.com.au`
- Meat House Canberra 的炸串/中式烤肉系统
- BBQ Town Dickson
- BBQ Town Belconnen
- 其他 Hanok 门店的菜单、价格、促销、打印配置

只要未来出现冲突，优先级如下：
1. Daniel 最新明确确认的信息
2. 当前 GitHub / Vercel / Supabase 的实时状态
3. 本 SSOT 中较新的记录
4. 历史聊天或旧文件

---

# 1. 项目身份 / Canonical Identity

## 1.1 当前 GitHub
**CONFIRMED — 2026-10-04**

- Repository：`397024611/hanok-unlimited-ordering`
- Default branch：`main`
- 当前权限：Admin / Push 可用
- Repository visibility：Public
- 当前项目名：`hanok-unlimited-ordering`

### 历史迁移
旧 GitHub owner：
- `GreatDaniel93/hanok-unlimited-ordering`

2026-10-04 已迁移/重新接通至：
- `397024611/hanok-unlimited-ordering`

**以后默认只使用 `397024611/hanok-unlimited-ordering`。**
旧 owner 只作为历史记录，不应再作为部署或开发基准。

---

## 1.2 当前 Vercel
**CONFIRMED — 2026-10-04**

- Project name：`hanok-unlimited-ordering-za6y`
- Project ID：`prj_GRf7V60vhKeYpaRjlTIZj9wRKR1m`
- Framework：Next.js
- Node.js：`22.x`
- Production domain：`orderhanokbbqwagga.com`
- Vercel fallback domain：`hanok-unlimited-ordering-za6y.vercel.app`

### 链路验证基准部署（SSOT 创建前）
- Deployment ID：`dpl_8Mw2SyYEHm75MTzzJPYXdGyhtj4J`
- State：`READY`
- Target：`production`
- Git branch：`main`
- Git commit：`1398b35cb717eb5978a30a776d1ed3a176cbd5b5`
- Commit message：`Remove Vercel linkage test file`
- GitHub source：`397024611/hanok-unlimited-ordering`

### 2026-10-04 链路测试
已完成：
1. GitHub → Vercel 测试 commit
2. Vercel 自动部署成功
3. 测试文件删除
4. 清理 commit 再次自动部署成功
5. Node 版本统一为 `22.x`

因此截至 2026-10-04：

**GitHub → Vercel production deployment 链路正常。**

---

## 1.3 当前域名
**CONFIRMED — 2026-10-04**

正式域名：
- `orderhanokbbqwagga.com`

状态：
- 已添加至 Wagga Vercel project
- `verified = true`
- 无 redirect

**禁止把 `meathouseskewer.com.au` 当作 Wagga 正式域名。**

---

## 1.4 当前 Supabase
**CONFIRMED — 2026-10-04**

- Project name：`hanokwagga buffet`
- Project ref / ID：`pycuztuyrhdkqlepbinh`
- Region：`ap-southeast-1`
- Status：`ACTIVE_HEALTHY`
- PostgreSQL major version：17

当前认定：

**Wagga production system 使用该 Supabase 项目。**

---

# 2. 当前 Web App 技术基线

**CONFIRMED FROM CURRENT REPO — 2026-10-04**

`package.json` 当前关键版本：
- Node：`22.x`
- Next.js：`16.3.0`
- React：`19.2.8`
- React DOM：`19.2.8`
- `@supabase/supabase-js`：`2.111.0`
- `qrcode`：`1.5.4`

项目性质：
- Next.js ordering app
- Supabase backend
- QR/table ordering
- Android Print Bridge source included in repo

当前 repo README 对 Android Bridge 的说明：
- Source folder：`android-print-bridge/`
- Android device 与打印机处于同一 LAN / Wi-Fi
- 打印机使用 ESC/POS over TCP 9100
- Bridge 从 Hanok cloud ordering API 拉取新订单
- Foreground service 常驻运行
- 只处理 Bridge 启动后创建的新订单

---

# 3. Wagga 产品定位 / Ordering Model

## 3.1 核心模式
**CONFIRMED**

Wagga 使用：

> **Unlimited Ordering / 点单式自助系统**

不是：
- Meat House 炸串项目
- Meat House skewer ordering system
- BBQ Town 系统

Wagga 重启时的核心要求：
- **No Starter / 不需要 starter platter**
- **所有肉类默认 50g / 份**

---

## 3.2 桌台 / QR / Session 逻辑
**CARRIED-FORWARD WAGGA BASELINE — 如未来代码或 Daniel 新确认冲突，以最新信息覆盖**

历史已确认的 Wagga 设计逻辑包括：
- 每桌一个 QR / table token
- 同一桌不同手机共享同一 session
- 同桌共享：
  - 用餐时间
  - cooldown
  - round limit
  - item limits
- 每轮允许下单数量：
  - `guest count × rate`
- 单品可设置：
  - Max
  - Unlimited / 不限制

历史 route pattern：
- `/t/[token]`

> 注意：该 route pattern 属于既有 Wagga 设计记录；未来如前端路由改版，需要同步更新本文件。

---

## 3.3 用餐时间规则
**CARRIED-FORWARD BASELINE**

- Session：90 分钟
- Last Order：结束前 15 分钟

Manager 可控制：
- Open table
- Close table
- Unlock table/session
- Extend time
- 修改 guest count
- Move table / 换桌

> 当前门店最终执行时长如发生变化，以最新运营确认覆盖。

---

# 4. Kitchen / KDS 逻辑

历史 Wagga 订单处理设计：

### KDS 三栏
1. 新单 / New
2. 准备中 / Preparing
3. 待取 / Ready / Pickup

厨房曾按类别区分：
- Meat
- Hot Kitchen / Hot Dish

系统设计目标：
- 前台 QR 下单
- 云端订单进入厨房看板
- 打印 Bridge 同步打印对应 station
- 避免同一 ticket 重复打印

**已处理过 per-ticket deduplication 问题。**

---

# 5. 100 串 / 累计奖励券逻辑

**CONFIRMED HISTORICAL PRODUCT RULE — 仍作为 Wagga baseline 保存**

累计数量跨越百位时触发奖励。

奖励档位：
- 100 → 5
- 200 → 10
- 300 → 15
- 400 → 20
- 500 后循环

规则：
- 一次操作跨过多个百位时，取**最高档奖励**
- 达标后生成 voucher / coupon
- Voucher **不能在中奖当次消费使用**
- 从中奖次日 `00:00` 起生效
- 有效期：30 天
- Single use / 只能核销一次
- 后台需要 Manager redemption / 核销页
- Winning screen 要提示顾客截图/保存，并展示使用条件

> 奖励数值单位如果未来对外展示，必须以当前系统配置为准；不要仅凭本文件推断货币符号。

---

# 6. Android Print Bridge

## 6.1 目标
Android 手机作为 Wagga 本地打印 Bridge：

Cloud Order API → Android Bridge → Local ESC/POS Printer

主要目标：
- 不依赖 PC
- Android 可后台长期运行
- 打印机只需要局域网 TCP 连接
- 订单按 station 分流
- 避免重复打印

默认打印协议：
- TCP
- Port：`9100`

---

## 6.2 历史打印机网络配置
**LAST-KNOWN / NEEDS REVERIFY BEFORE ONSITE CHANGE**

历史 Wagga 配置记录：
- `.232` → TOTAL ORDER（Meat + Hot Dish）
- `.231` → SPLIT（Meat / Hot Kitchen）
- TCP port：`9100`

这些 IP 是历史现场配置，未来进行打印机改动前必须再次确认现场 LAN IP。

---

## 6.3 Bridge 稳定性设计
v1.5 阶段提出的长期运行要求：
- Foreground Service
- CPU Wake Lock
- Wi-Fi Lock
- Sticky restart
- Boot recovery
- App update recovery
- Reconnect backoff
- Heartbeat
- 锁屏状态继续运行

限制：
- Android 用户主动 **Force Stop** 后，系统通常无法自动恢复，必须重新打开 App。

---

## 6.4 v1.10 / v1.11 历史

### v1.10
Git/Vercel 历史 deployment metadata 中存在：
- `Bump print bridge to v1.10`
- `Build bilingual print bridge v1.10`
- `Save Android v1.10 build result`

这些 deployment 来自旧 GitHub owner `GreatDaniel93`，现在仅作为历史记录。

### v1.11 签名基线
2026-09-25 曾生成 v1.11 Release APK。

记录 SHA-256：
`4a1bb80504cbbc8f2b3d1668893f4a4da11ad4463b040188566de5517a8eb0cd`

设计目标：
- 使用一致 signing baseline
- 以后可以直接覆盖升级，而不是每次卸载重装

当时注意事项：
- 如果旧版本是 debug signing，首次切换到永久 signing 版本时可能需要先卸载旧 app。

### v1.11 P1-OFF variant
曾制作：
- Printer 1 强制关闭
- Printer 2 / Printer 3 继续工作

注意：
- 该版本是 **hard-coded P1 OFF**
- 不是带 UI toggle 的版本

### v1.11 已知问题
用户实际测试曾反馈：
- APK 安装不成功 / 安装异常
- 点击 `START BRIDGE` 后闪退
- 没有看到预期的 `Disable Print 1` UI button

因此：

> **不得把 v1.11 直接视为“已稳定上线版本”。**

当前现场真正稳定运行的 APK version / build 仍需要在下一次现场或 Android Studio 验证时确认，并写回本文件。

---

# 7. GitHub Actions / APK Build

当前 repo README 记录：

Workflow：
- `.github/workflows/android-print-bridge.yml`

功能：
- Android Bridge 相关文件修改后自动 build debug APK

Artifact name：
- `hanok-print-bridge-debug-apk`

未来正式 release build 应继续遵守：
- 固定 application ID
- 固定 signing key
- versionCode 递增
- versionName 清晰
- 保存 release SHA-256

目标：

> 后续 App 更新可以覆盖安装，不需要重复卸载。

---

# 8. 当前技术链路状态快照

## 2026-10-04 13:27 AEDT

| Layer | Canonical | Status |
|---|---|---|
| GitHub | `397024611/hanok-unlimited-ordering` | ✅ Connected |
| Branch | `main` | ✅ Active |
| Vercel | `hanok-unlimited-ordering-za6y` | ✅ Connected |
| Verified chain deploy | `dpl_8Mw2SyYEHm75MTzzJPYXdGyhtj4J` | ✅ READY |
| Node | `22.x` | ✅ Aligned |
| Domain | `orderhanokbbqwagga.com` | ✅ Verified |
| Supabase | `hanokwagga buffet` | ✅ ACTIVE_HEALTHY |
| Supabase ref | `pycuztuyrhdkqlepbinh` | ✅ Canonical |

当前判断：

**GitHub → Vercel → Domain → Supabase 主链路通顺。**

---

# 9. Deprecated / Historical — 不再作为当前基准

## 9.1 旧 GitHub owner
- `GreatDaniel93/hanok-unlimited-ordering`

状态：Historical / replaced by `397024611/hanok-unlimited-ordering`.

---

## 9.2 错误混淆链路
以下内容过去曾被错误写入 Wagga 资料或混在其他知识库中：

- `GreatDaniel93/meathouse-skewer-ordering`
- Vercel `meathouse-skewer-ordering`
- Domain `meathouseskewer.com.au`

这些**不能再作为 Hanok Wagga canonical technical identity**。

---

# 10. 门店实体信息

历史记录曾出现：
- Hanok Wagga Wagga
- 地址：`85 Baylis Street, Wagga Wagga`

该地址来自较早记录。

**状态：HISTORICAL / CURRENT ADDRESS NOT REVERIFIED IN THIS SSOT UPDATE**

如未来进行地图、Google Business、Council、租约、门店资料更新，先确认当前地址再把状态改为 CONFIRMED。

---

# 11. 当前仍需确认的项目

以下项目不要凭旧资料自行推断：

1. 当前 Wagga 实际桌数
2. 当前成人/儿童价格
3. 当前营业时间
4. 当前完整菜单
5. 当前每轮 `rate` 数值
6. 当前 cooldown 数值
7. 当前 item Max 配置
8. `/t/[token]` 是否仍为 production customer route
9. 当前现场 printer IP
10. `.231` / `.232` 当前实际 station mapping
11. 当前现场正在运行的 Android APK version
12. v1.11 crash 是否已修复
13. 当前是否已经有 Printer 1 UI disable toggle
14. 当前 signing key 是否已经完全固定为 production upgrade baseline
15. 当前桌台数量是否仍为历史设计的 15 tables

这些项目未来一旦确认，应立即移动到对应 CONFIRMED section，并在 Change Log 写入日期。

---

# 12. 防混淆检查清单

每次处理 Wagga 请求前，先确认以下五件事：

- [ ] Repo 是不是 `397024611/hanok-unlimited-ordering`？
- [ ] Vercel 是不是 `hanok-unlimited-ordering-za6y`？
- [ ] Domain 是不是 `orderhanokbbqwagga.com`？
- [ ] Supabase 是不是 `hanokwagga buffet / pycuztuyrhdkqlepbinh`？
- [ ] 当前需求是不是 Wagga Unlimited Ordering，而不是 Meat House / BBQ Town？

如果其中任何一项不一致：

**停止套用其他项目资料，先解决 identity conflict。**

---

# 13. 更新规则 / Maintenance Protocol

以后只要 Hanok Wagga 出现新的“已确认”信息，包括但不限于：

- GitHub repo / branch
- Vercel project / deployment
- Domain
- Supabase
- Database schema
- Menu
- Price
- Table rules
- Session time
- Ordering limits
- KDS
- Printer
- Android Bridge
- APK
- Signing
- QR
- Voucher
- Promotion
- Store operation
- Opening hours
- Hardware / LAN
- Staff workflow

都应该：

1. 更新本文件对应章节
2. 把旧信息标成 Historical / Deprecated，而不是直接丢失上下文
3. 在 Change Log 新增一条
4. 技术 identity 变更时同步修改顶部 Canonical Identity
5. 不从 Meat House、BBQ Town 或其他 Hanok 项目复制未经确认的数据

---

# 14. Change Log

## 2026-10-04 — SSOT v1.0
创建 Hanok Wagga 独立长期记忆文件。

确认并锁定：
- Wagga = Unlimited Ordering
- GitHub → `397024611/hanok-unlimited-ordering`
- Vercel → `hanok-unlimited-ordering-za6y`
- Domain → `orderhanokbbqwagga.com`
- Supabase → `hanokwagga buffet`
- Supabase ref → `pycuztuyrhdkqlepbinh`
- Node → `22.x`
- GitHub → Vercel 自动部署测试成功
- 测试文件已清理
- 链路验证 production deployment READY
- 明确废弃旧 GreatDaniel93 canonical identity
- 明确禁止把 Meat House / `meathouse-skewer-ordering` / `meathouseskewer.com.au` 混入 Wagga
- 保存 Wagga ordering、voucher、print bridge、v1.11 signing/P1-OFF 以及 crash 风险的历史基线

---

# 15. 一句话项目摘要

> **Hanok Wagga 是独立的 Unlimited Ordering 点单式自助项目；当前 canonical 技术链路是 `397024611/hanok-unlimited-ordering` → Vercel `hanok-unlimited-ordering-za6y` → `orderhanokbbqwagga.com` → Supabase `hanokwagga buffet (pycuztuyrhdkqlepbinh)`，任何 Meat House / BBQ Town 数据都不得混入。**
