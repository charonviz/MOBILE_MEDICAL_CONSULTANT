# Theming & Design System

## Color Palette

| Token              | Light          | Dark           | Usage              |
|--------------------|----------------|----------------|-------------------|
| colorPrimary       | #4A7DFF        | #4A7DFF        | Buttons, links     |
| Gold accent        | #C8A97E        | #C8A97E        | Subtitle, branding |
| Background         | #FFFFFF        | #08090C        | Screen background  |
| Surface            | #F2F3F7        | #13151C        | Cards              |
| Text primary       | #1A1B1F        | #E4E6EA        | Body text          |
| Text secondary     | #898B95        | #898B95        | Captions           |

## Chat Bubble Gradient

User messages use a CSS-style linear gradient at 135°:
- Start: `#5B8DEF`
- End: `#3A6FE8`

Defined in `res/drawable/bg_bubble_user.xml` as an Android shape gradient.

## Severity Colors

- Emergency: `#FF1744` (Material Red A400)
- High: `#FF6D00` (Material Orange A700)
- Medium: `#2979FF` (Material Blue A400)
- Low: `#00C853` (Material Green A400)

## Typography

- Title: 30sp bold (home screen)
- Card title: 16sp bold
- Card subtitle: 12sp regular
- Chat text: 14sp regular
- Disclaimer: 11sp light

## App Icon

The app icon is a gold caduceus on a dark rounded-square background. It is provided in 5 Android density buckets (mdpi through xxxhdpi) plus a 512×512 Play Store asset.

## Splash Screen

2.5-second fade-in animation of the gold caduceus logo on a deep black (#08090C) background, followed by a crossfade transition to the main screen.
