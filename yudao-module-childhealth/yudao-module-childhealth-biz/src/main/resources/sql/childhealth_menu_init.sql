INSERT IGNORE INTO `system_menu` (`id`, `name`, `permission`, `type`, `sort`, `parent_id`, `path`, `icon`, `component`, `status`, `visible`, `keep_alive`, `always_show`, `creator`, `create_time`, `updater`, `update_time`, `deleted`) VALUES
(5146, '总检审核', 'childhealth:checkup:review', 2, 5, 5100, '#', 'ep:check-circle', 'childhealth/checkup/batchReview', 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0');

INSERT IGNORE INTO `system_menu` (`id`, `name`, `permission`, `type`, `sort`, `parent_id`, `path`, `icon`, `component`, `status`, `visible`, `keep_alive`, `always_show`, `creator`, `create_time`, `updater`, `update_time`, `deleted`) VALUES
(5147, '体检审核', 'childhealth:checkup:review', 2, 6, 5100, '#', 'ep:audit', 'childhealth/checkup/index', 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0');

INSERT IGNORE INTO `system_menu` (`id`, `name`, `permission`, `type`, `sort`, `parent_id`, `path`, `icon`, `component`, `status`, `visible`, `keep_alive`, `always_show`, `creator`, `create_time`, `updater`, `update_time`, `deleted`) VALUES
(5106, '专案登记', 'childhealth:case:query', 1, 6, 5100, 'childhealth/case', 'ep:folder-opened', '', 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0');

INSERT IGNORE INTO `system_menu` (`id`, `name`, `permission`, `type`, `sort`, `parent_id`, `path`, `icon`, `component`, `status`, `visible`, `keep_alive`, `always_show`, `creator`, `create_time`, `updater`, `update_time`, `deleted`) VALUES
(5107, '专案工作台', 'childhealth:case:query', 2, 1, 5106, '#', 'ep:list', 'childhealth/case/caseWorkbench/index', 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0');
