# ModBadges
An API to create uniform badges for [Modrinth](https://modrinth.com/) and [CurseForge](https://www.curseforge.com/) projects.

## Modrinth badges
- `/modrinth/downloads/<project_id>` ➡️ ![](https://badges.moddingx.org/modrinth/downloads/libx)
- `/modrinth/versions/<project_id>` ➡️ ![](https://badges.moddingx.org/modrinth/versions/libx)

## CurseForge badges
- `/curseforge/downloads/<project_id>` ➡️ ![](https://badges.moddingx.org/curseforge/downloads/412525)
- `/curseforge/versions/<project_id>` ➡️ ![](https://badges.moddingx.org/curseforge/versions/412525)

## Styles
You can also set the badge style. Use `?style=flat` at the end of your request. If you specify a style which isn't
available, it falls back to `default`.

Available styles:
- `default` ➡️ ![](https://badges.moddingx.org/modrinth/downloads/libx?style=default)
- `flat` ➡️ ![](https://badges.moddingx.org/modrinth/downloads/libx?style=flat)
