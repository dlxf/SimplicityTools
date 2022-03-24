package com.lt2333.simplicitytools.activity

import android.annotation.SuppressLint
import android.content.ComponentName
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Switch
import android.widget.Toast
import cn.fkj233.ui.activity.MIUIActivity
import cn.fkj233.ui.activity.OwnSP
import cn.fkj233.ui.activity.data.MIUIPopupData
import cn.fkj233.ui.activity.view.SpinnerV
import cn.fkj233.ui.activity.view.SwitchV
import cn.fkj233.ui.activity.view.TextSummaryV
import cn.fkj233.ui.activity.view.TextV
import cn.fkj233.ui.dialog.MIUIDialog
import com.lt2333.simplicitytools.BuildConfig
import com.lt2333.simplicitytools.R
import com.lt2333.simplicitytools.util.SPUtils
import com.lt2333.simplicitytools.util.ShellUtils
import kotlin.system.exitProcess

class SettingsActivity : MIUIActivity() {
    private val handler by lazy { Handler(Looper.getMainLooper()) }

    private val activity = this

    override fun onCreate(savedInstanceState: Bundle?) {
        checkLSPosed()
        initView {
            //主页
            registerMain("Home") {

                TextWithSwitch(
                    TextV(resId = R.string.main_switch, colorId = R.color.purple_700),
                    SwitchV("main_switch", true)
                )
                TextWithSwitch(
                    TextV(resId = R.string.HideLauncherIcon),
                    SwitchV("hLauncherIcon", customOnCheckedChangeListener = {
                        packageManager.setComponentEnabledSetting(
                            ComponentName(activity, "${BuildConfig.APPLICATION_ID}.launcher"),
                            if (it) {
                                PackageManager.COMPONENT_ENABLED_STATE_DISABLED
                            } else {
                                PackageManager.COMPONENT_ENABLED_STATE_ENABLED
                            },
                            PackageManager.DONT_KILL_APP
                        )
                    })
                )
                TextSummaryArrow(
                    TextSummaryV(
                        textId = R.string.matters_needing_attention,
                        colorId = R.color.red,
                        onClickListener = {
                            MIUIDialog(activity) {
                                setTitle(R.string.matters_needing_attention)
                                setMessage(
                                    R.string.matters_needing_attention_context
                                )
                                setRButton(R.string.Done) {
                                    dismiss()
                                }
                            }.show()
                        })
                )
                Line()
                TitleText(resId = R.string.scope)
                TextSummaryArrow(
                    TextSummaryV(
                        textId = R.string.scope_systemui, tipsId = R.string.scope_systemui_summary,
                        onClickListener = { showFragment(getString(R.string.scope_systemui)) }
                    )
                )
                TextSummaryArrow(
                    TextSummaryV(
                        textId = R.string.scope_android, tipsId = R.string.scope_android_summary,
                        onClickListener = { showFragment(getString(R.string.scope_android)) }
                    )
                )
                TextSummaryArrow(
                    TextSummaryV(
                        textId = R.string.scope_other, tipsId = R.string.scope_other_summary,
                        onClickListener = { showFragment(getString(R.string.scope_other)) }
                    )
                )
                Line()
                TitleText(resId = R.string.about)
                TextSummaryArrow(
                    TextSummaryV(
                        textId = R.string.about_module,
                        tips = getString(R.string.about_module_summary),
                        onClickListener = { showFragment(getString(R.string.about_module)) }
                    )
                )
            }
            //菜单页
            registerMenu("Menu") {
                TextSummaryArrow(
                    TextSummaryV(textId = R.string.reboot, onClickListener = {
                        MIUIDialog(activity) {
                            setTitle(R.string.Tips)
                            setMessage(R.string.are_you_sure_reboot)
                            setLButton(R.string.cancel) {
                                dismiss()
                            }
                            setRButton(R.string.Done) {
                                val command = arrayOf("reboot")
                                ShellUtils.execCommand(command, true)
                                dismiss()
                            }
                        }.show()
                    })
                )
                TextSummaryArrow(
                    TextSummaryV(textId = R.string.reboot_host, onClickListener = {
                        MIUIDialog(activity) {
                            setTitle(R.string.Tips)
                            setMessage(R.string.are_you_sure_reboot_scope)
                            setLButton(R.string.cancel) {
                                dismiss()
                            }
                            setRButton(R.string.Done) {
                                val command = arrayOf(
                                    "killall com.android.systemui",
                                    "killall com.miui.home",
                                    "killall com.miui.securitycenter ",
                                    "killall com.android.settings",
                                    "killall com.miui.powerkeeper",
                                    "killall com.android.updater",
                                    "killall com.miui.mediaeditor",
                                    "killall com.miui.screenshot"
                                )
                                ShellUtils.execCommand(command, true)
                                dismiss()
                            }
                        }.show()
                    })
                )

            }
            //系统界面
            register(getString(R.string.scope_systemui), getString(R.string.scope_systemui)) {
                TitleText(resId = R.string.statusbar)

                TextSummaryWithSwitch(
                    TextSummaryV(
                        textId = R.string.big_mobile_type_icon
                    ),
                    SwitchV("big_mobile_type_icon")
                )

                TextSummaryWithSwitch(
                    TextSummaryV(
                        textId = R.string.hide_battery_percentage_icon,
                        tipsId = R.string.hide_battery_percentage_icon_summary
                    ),
                    SwitchV("hide_battery_percentage_icon")
                )

                TextSummaryWithSwitch(
                    TextSummaryV(
                        textId = R.string.remove_the_maximum_number_of_notification_icons,
                        tipsId = R.string.remove_the_maximum_number_of_notification_icons_summary
                    ),
                    SwitchV("remove_the_maximum_number_of_notification_icons")
                )

                TextSummaryWithSwitch(
                    TextSummaryV(
                        textId = R.string.double_tap_to_sleep
                    ), SwitchV("status_bar_double_tap_to_sleep")
                )

                Line()
                TitleText(resId = R.string.status_bar_layout)

                TextSummaryWithSwitch(
                    TextSummaryV(
                        textId = R.string.status_bar_time_center,
                        tipsId = R.string.status_bar_layout_summary
                    ),
                    SwitchV("status_bar_time_center")
                )

                val layout_compatibility_mode_binding = GetDataBinding(
                    SPUtils.getBoolean(
                        activity,
                        "layout_compatibility_mode",
                        false
                    )
                ) { view, flags, data ->
                    when (flags) {
                        1 -> (view as Switch).isEnabled = data as Boolean
                        2 -> view.visibility = if (data as Boolean) View.VISIBLE else View.GONE
                    }
                }

                TextSummaryWithSwitch(
                    TextSummaryV(
                        textId = R.string.layout_compatibility_mode,
                        tipsId = R.string.layout_compatibility_mode_summary
                    ),
                    SwitchV(
                        "layout_compatibility_mode",
                        dataBindingSend = layout_compatibility_mode_binding.bindingSend
                    )
                )

                Text(
                    resId = R.string.left_margin,
                    dataBindingRecv = layout_compatibility_mode_binding.binding.getRecv(2)
                )


                SeekBarWithText(
                    "status_bar_left_margin",
                    0,
                    300,
                    0,
                    dataBindingRecv = layout_compatibility_mode_binding.binding.getRecv(2)
                )



                Text(
                    resId = R.string.right_margin,
                    dataBindingRecv = layout_compatibility_mode_binding.binding.getRecv(2)
                )


                SeekBarWithText(
                    "status_bar_right_margin",
                    0,
                    300,
                    0,
                    dataBindingRecv = layout_compatibility_mode_binding.binding.getRecv(2)
                )



                Line()
                TitleText(resId = R.string.status_bar_clock_format)

                val custom_clock_binding = GetDataBinding(
                    SPUtils.getBoolean(
                        activity,
                        "custom_clock_switch",
                        false
                    )
                ) { view, flags, data ->
                    when (flags) {
                        1 -> (view as Switch).isEnabled = data as Boolean
                        2 -> view.visibility = if (data as Boolean) View.VISIBLE else View.GONE
                    }
                }



                TextWithSwitch(
                    TextV(resId = R.string.custom_clock_switch, colorId = R.color.purple_700),
                    SwitchV(
                        "custom_clock_switch",
                        dataBindingSend = custom_clock_binding.bindingSend
                    )
                )


                TextWithSwitch(
                    TextV(resId = R.string.status_bar_time_year),
                    SwitchV("status_bar_time_year"),
                    dataBindingRecv = custom_clock_binding.binding.getRecv(2)
                )


                TextWithSwitch(
                    TextV(resId = R.string.status_bar_time_month),
                    SwitchV("status_bar_time_month"),
                    dataBindingRecv = custom_clock_binding.binding.getRecv(2)
                )


                TextWithSwitch(
                    TextV(resId = R.string.status_bar_time_day),
                    SwitchV("status_bar_time_day"),
                    dataBindingRecv = custom_clock_binding.binding.getRecv(2)
                )


                TextWithSwitch(
                    TextV(resId = R.string.status_bar_time_week),
                    SwitchV("status_bar_time_week"),
                    dataBindingRecv = custom_clock_binding.binding.getRecv(2)
                )


                TextWithSwitch(
                    TextV(resId = R.string.status_bar_time_double_hour),
                    SwitchV("status_bar_time_double_hour"),
                    dataBindingRecv = custom_clock_binding.binding.getRecv(2)
                )


                TextWithSwitch(
                    TextV(resId = R.string.status_bar_time_period),
                    SwitchV("status_bar_time_period", true),
                    dataBindingRecv = custom_clock_binding.binding.getRecv(2)
                )


                TextWithSwitch(
                    TextV(resId = R.string.status_bar_time_seconds),
                    SwitchV("status_bar_time_seconds"),
                    dataBindingRecv = custom_clock_binding.binding.getRecv(2)
                )


                TextWithSwitch(
                    TextV(resId = R.string.status_bar_time_hide_space),
                    SwitchV("status_bar_time_hide_space"),
                    dataBindingRecv = custom_clock_binding.binding.getRecv(2)
                )


                TextWithSwitch(
                    TextV(resId = R.string.status_bar_time_double_line),
                    SwitchV("status_bar_time_double_line"),
                    dataBindingRecv = custom_clock_binding.binding.getRecv(2)
                )


                Text(
                    resId = R.string.status_bar_clock_size,
                    dataBindingRecv = custom_clock_binding.binding.getRecv(2)
                )


                SeekBarWithText(
                    "status_bar_clock_size", 0, 18, 0,
                    dataBindingRecv = custom_clock_binding.binding.getRecv(2)
                )


                Text(
                    resId = R.string.status_bar_clock_double_line_size,
                    dataBindingRecv = custom_clock_binding.binding.getRecv(2)
                )


                SeekBarWithText(
                    "status_bar_clock_double_line_size", 0, 9, 0,
                    dataBindingRecv = custom_clock_binding.binding.getRecv(2)
                )


                Line()
                TitleText(resId = R.string.status_bar_icon)

                TextSummaryArrow(
                    TextSummaryV(
                        textId = R.string.hide_icon,
                        onClickListener = { showFragment(getString(R.string.hide_icon)) }
                    )
                )

                Line()
                TitleText(resId = R.string.status_bar_network_speed)

                TextSummaryWithSwitch(
                    TextSummaryV(
                        textId = R.string.status_bar_network_speed_refresh_speed,
                        tipsId = R.string.status_bar_network_speed_refresh_speed_summary
                    ),
                    SwitchV("status_bar_network_speed_refresh_speed")
                )


                TextSummaryWithSwitch(
                    TextSummaryV(
                        textId = R.string.hide_status_bar_network_speed_second,
                        tipsId = R.string.hide_status_bar_network_speed_second_summary
                    ),
                    SwitchV("hide_status_bar_network_speed_second")
                )


                TextWithSwitch(
                    TextV(resId = R.string.hide_network_speed_splitter),
                    SwitchV("hide_network_speed_splitter")
                )


                val status_bar_dual_row_network_speed_binding = GetDataBinding(
                    SPUtils.getBoolean(
                        activity,
                        "status_bar_dual_row_network_speed",
                        false
                    )
                ) { view, flags, data ->
                    when (flags) {
                        1 -> (view as Switch).isEnabled = data as Boolean
                        2 -> view.visibility = if (data as Boolean) View.VISIBLE else View.GONE
                    }
                }


                TextSummaryWithSwitch(
                    TextSummaryV(
                        textId = R.string.status_bar_dual_row_network_speed,
                        tipsId = R.string.status_bar_dual_row_network_speed_summary
                    ),
                    SwitchV(
                        "status_bar_dual_row_network_speed",
                        dataBindingSend = status_bar_dual_row_network_speed_binding.bindingSend
                    )
                )

                val align: HashMap<Int, String> = hashMapOf()
                align[0] = getString(R.string.left)
                align[1] = getString(R.string.right)

                TextWithSpinner(
                    TextV(resId = R.string.status_bar_network_speed_dual_row_gravity),
                    SpinnerV(
                        arrayListOf<MIUIPopupData>().apply {
                            add(MIUIPopupData(getString(R.string.left)) {
                                OwnSP.ownSP.edit().run {
                                    putInt(
                                        "status_bar_network_speed_dual_row_gravity",
                                        0
                                    )
                                    apply()
                                }
                            })
                            add(MIUIPopupData(getString(R.string.right)) {
                                OwnSP.ownSP.edit().run {
                                    putInt(
                                        "status_bar_network_speed_dual_row_gravity",
                                        1
                                    )
                                    apply()
                                }
                            })
                        }, currentValue = align[OwnSP.ownSP.getInt(
                            "status_bar_network_speed_dual_row_gravity",
                            0
                        )].toString()
                    ),
                    dataBindingRecv = status_bar_dual_row_network_speed_binding.binding.getRecv(2)
                )


                TextWithSpinner(
                    TextV(resId = R.string.status_bar_network_speed_dual_row_icon),
                    SpinnerV(
                        arrayListOf<MIUIPopupData>().apply {
                            add(MIUIPopupData(getString(R.string.none)) {
                                OwnSP.ownSP.edit().run {
                                    putString(
                                        "status_bar_network_speed_dual_row_icon",
                                        getString(R.string.none)
                                    )
                                    apply()
                                }
                            })
                            add(MIUIPopupData("▲▼") {
                                OwnSP.ownSP.edit().run {
                                    putString("status_bar_network_speed_dual_row_icon", "▲▼")
                                    apply()
                                }
                            })
                            add(MIUIPopupData("△▽") {
                                OwnSP.ownSP.edit().run {
                                    putString("status_bar_network_speed_dual_row_icon", "△▽")
                                    apply()
                                }
                            })
                            add(MIUIPopupData("↑↓") {
                                OwnSP.ownSP.edit().run {
                                    putString("status_bar_network_speed_dual_row_icon", "↑↓")
                                    apply()
                                }
                            })
                        }, "${
                            OwnSP.ownSP.getString(
                                "status_bar_network_speed_dual_row_icon",
                                getString(R.string.none)
                            )
                        }"
                    ),
                    dataBindingRecv = status_bar_dual_row_network_speed_binding.binding.getRecv(2)
                )


                Text(
                    resId = R.string.status_bar_network_speed_dual_row_size,
                    dataBindingRecv = status_bar_dual_row_network_speed_binding.binding.getRecv(2)
                )


                SeekBarWithText(
                    "status_bar_network_speed_dual_row_size",
                    0,
                    9,
                    0,
                    dataBindingRecv = status_bar_dual_row_network_speed_binding.binding.getRecv(2)
                )



                Line()
                TitleText(resId = R.string.notification_center)
                val show_weather_main_switch_binding = GetDataBinding(
                    SPUtils.getBoolean(
                        activity,
                        "notification_weather",
                        false
                    )
                ) { view, flags, data ->
                    when (flags) {
                        1 -> (view as Switch).isEnabled = data as Boolean
                        2 -> view.visibility = if (data as Boolean) View.VISIBLE else View.GONE
                    }
                }

                TextSummaryWithSwitch(
                    TextSummaryV(
                        textId = R.string.show_weather_main_switch, colorId = R.color.purple_700
                    ),
                    SwitchV(
                        "notification_weather",
                        dataBindingSend = show_weather_main_switch_binding.bindingSend
                    )
                )


                TextSummaryWithSwitch(
                    TextSummaryV(
                        textId = R.string.show_city,
                    ),
                    SwitchV("notification_weather_city"),
                    dataBindingRecv = show_weather_main_switch_binding.binding.getRecv(2)
                )


                TextSummaryWithSwitch(
                    TextSummaryV(
                        textId = R.string.can_notification_slide,
                    ),
                    SwitchV("can_notification_slide")
                )


                Line()
                TitleText(resId = R.string.control_center)
                val control_center_weather_binding = GetDataBinding(
                    SPUtils.getBoolean(
                        activity,
                        "control_center_weather",
                        false
                    )
                ) { view, flags, data ->
                    when (flags) {
                        1 -> (view as Switch).isEnabled = data as Boolean
                        2 -> view.visibility = if (data as Boolean) View.VISIBLE else View.GONE
                    }
                }

                TextSummaryWithSwitch(
                    TextSummaryV(
                        textId = R.string.show_weather_main_switch,
                        colorId = R.color.purple_700,
                        tipsId = R.string.control_center_weather_summary
                    ),
                    SwitchV(
                        "control_center_weather",
                        dataBindingSend = control_center_weather_binding.bindingSend
                    )
                )


                TextSummaryWithSwitch(
                    TextSummaryV(
                        textId = R.string.show_city,
                    ),
                    SwitchV("control_center_weather_city"),
                    dataBindingRecv = control_center_weather_binding.binding.getRecv(2)
                )


                Line()
                TitleText(resId = R.string.lock_screen)

                TextSummaryWithSwitch(
                    TextSummaryV(
                        textId = R.string.remove_the_left_side_of_the_lock_screen,
                        tipsId = R.string.only_official_default_themes_are_supported
                    ),
                    SwitchV("remove_the_left_side_of_the_lock_screen")
                )


                TextSummaryWithSwitch(
                    TextSummaryV(
                        textId = R.string.remove_lock_screen_camera,
                        tipsId = R.string.only_official_default_themes_are_supported
                    ),
                    SwitchV("remove_lock_screen_camera")
                )


                TextSummaryWithSwitch(
                    TextSummaryV(
                        textId = R.string.enable_wave_charge_animation
                    ),
                    SwitchV("enable_wave_charge_animation")
                )


                TextSummaryWithSwitch(
                    TextSummaryV(
                        textId = R.string.lock_screen_charging_current,
                        tipsId = R.string.only_official_default_themes_are_supported
                    ),
                    SwitchV("lock_screen_charging_current")
                )


                TextSummaryWithSwitch(
                    TextSummaryV(
                        textId = R.string.double_tap_to_sleep,
                        tipsId = R.string.home_double_tap_to_sleep_summary
                    ), SwitchV("lock_screen_double_tap_to_sleep")
                )


                Line()
                TitleText(resId = R.string.old_quick_settings_panel)
                val old_qs_custom_switch_binding = GetDataBinding(
                    SPUtils.getBoolean(
                        activity,
                        "old_qs_custom_switch",
                        false
                    )
                ) { view, flags, data ->
                    when (flags) {
                        1 -> (view as Switch).isEnabled = data as Boolean
                        2 -> view.visibility = if (data as Boolean) View.VISIBLE else View.GONE
                    }
                }

                TextSummaryWithSwitch(
                    TextSummaryV(
                        textId = R.string.old_qs_custom_switch,
                        colorId = R.color.purple_700
                    ),
                    SwitchV(
                        "old_qs_custom_switch",
                        dataBindingSend = old_qs_custom_switch_binding.bindingSend
                    )
                )



                Text(
                    resId = R.string.qs_custom_rows,
                    dataBindingRecv = old_qs_custom_switch_binding.binding.getRecv(2)
                )


                SeekBarWithText(
                    "qs_custom_rows",
                    1,
                    6,
                    3,
                    dataBindingRecv = old_qs_custom_switch_binding.binding.getRecv(2)
                )




                Text(
                    resId = R.string.qs_custom_rows_horizontal,
                    dataBindingRecv = old_qs_custom_switch_binding.binding.getRecv(2)
                )


                SeekBarWithText(
                    "qs_custom_rows_horizontal",
                    1,
                    3,
                    2,
                    dataBindingRecv = old_qs_custom_switch_binding.binding.getRecv(2)
                )



                Text(
                    resId = R.string.qs_custom_columns,
                    dataBindingRecv = old_qs_custom_switch_binding.binding.getRecv(2)
                )


                SeekBarWithText(
                    "qs_custom_columns",
                    1,
                    7,
                    4,
                    dataBindingRecv = old_qs_custom_switch_binding.binding.getRecv(2)
                )


                Text(
                    resId = R.string.qs_custom_columns_unexpanded,
                    dataBindingRecv = old_qs_custom_switch_binding.binding.getRecv(2)
                )


                SeekBarWithText(
                    "qs_custom_columns_unexpanded",
                    1,
                    7,
                    5,
                    dataBindingRecv = old_qs_custom_switch_binding.binding.getRecv(2)
                )

            }
        }
        super.onCreate(savedInstanceState)
    }

    //检测LSPosed是否激活
    @SuppressLint("WorldReadableFiles")
    private fun checkLSPosed() {
        try {
            setSP(getSharedPreferences("config", MODE_WORLD_READABLE))
        } catch (exception: SecurityException) {
            isLoad = false
            MIUIDialog(this) {
                setTitle(R.string.Tips)
                setMessage(R.string.not_support)
                setCancelable(false)
                setRButton(R.string.Done) {
                    exitProcess(0)
                }
            }.show()
        }
    }

    fun showToast(string: String) {
        handler.post {
            Toast.makeText(this, string, Toast.LENGTH_LONG).show()
        }
    }
}