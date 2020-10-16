# Orature-Plugin
A base repo to create an audio plugin for Orature

# Background and Problem Introduction

Orature supports third-party (external applications) and first-party (which can be dynamically loaded in window) plugins for editing and recording. 
Third party plugins are launched by calling exec() on the provided third-party binary, and providing cli arguments, namely --wav="{file}" where {file} is the absolute path to a wav file (in the case of recording, this is an empty wav file with a valid header corresponding to 44.1khz 16 bit mono).

Implications of this are that the file passed to the external application serves as the return value of the plugin. In other words, Orature expects that any modifications to the file will overwrite the file passed to the plugin by Orature.

#### The following editors are confirmed to be content with this approach and will happily overwrite the file on save:
 - Adobe Audition
 - Ocenaudio

#### The following editors and Digital Audio Workstations are confirmed NOT compatible with this approach:
 - Audacity
 - Cubase
 - Logic Pro
 - Reaper

Applications that are not compatible tend to share a commonality. Namely, the incompatible software prefers to construct its own audio projects to operate off of, and requires the resulting audio to be exported, rendered, or "bounced."

# Proposed Solution

As a workaround, we propose small adapter applications sit in between Orature and the desired third party application. This adapter will be called by Orature AS the third party plugin (henceforth referred to as the adapter), and provide to it over the cli the wav file to be edited or recorded over via --wav="{file}". The adapter may then need to create and manage a project file corresponding to the specific application it is an adapter for. The association between this project and the original file provided through Orature may be done through some sort of persistent map of wav file to project file/directory, or a small database. Alternatively, the adapter could choose to create a new project from the file every time. Regardless of approach, the adapter should be responsible for launching the third party application, and, on the application's closing, be responsible for rendering the audio to the file Orature provided. Thus, when the adapter is finished, all changes made to the file shall overwrite the file allowing Orature to be naive of the intermediate steps necessary.

As none of this requires Java as a platform, the adapter can theoretically be implemented in any ecosystem or language. However, As Orature can run on any operating system supporting the JVM and JavaFX (which additionally implies both x86 and ARM architectures) which means that choosing NOT to implement the adapter in Java means needing to provide support on all platforms (this can, of course, be restricted to the platforms supported by the third party application in question). Platforms such as Python and Ruby requiring a runtime would preferrably be avoided as it would require bundling these runtimes with the Orature installer as they cannot be assumed to exist on the end user's computer.
