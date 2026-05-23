## [3.0.0] - 2026-05-23
### Added
- **Multi-modal Search**: Support for image files.
- **Image Processor**: Dominant color extraction.
- **Strategy Pattern**: Decoupled indexing logic for text and images.
- **Decorator Pattern**: Query pre-processor pipeline (Sanitization, Synonym expansion, Logic enhancement).
- **Factory Pattern**: Context-aware UI widgets (Gallery View, Code Metrics).

### Changed
- **Performance Optimization**: Refactored Path Scoring and Synonym expansion to use HashMap lookups
- **UI Conditional Rendering**: Hide the "CONTEXT" label when snippets are empty (optimized for image results).


## [2.0.0] - 2026-04-29
### Added
- **Observer Pattern**: Implementation of search activity tracking.
- **Predictive Search**: Real-time query suggestions in the UI based on historical data.
- **Ranking System**: Score, Last modified/accessed, alphabetical.
- **Swappable Strategies**: Toggle between the ordering strategies.
- **Query Parser**: Manage input using qualifiers like "content:" or "path:".

### Changed
- **UI**: Transitioned from console-based interaction to a multi-tabbed GUI.



## [1.0.0] - 2026-03-31
### Added
- **Incremental Indexing**: SHA-256 Checksum change detection to prevent redundant processing.
- **Full-Text Search**: Integration of SQLite FTS5 for high-speed keyword matching.
- **Initial Indexing Engine**: Recursive directory traversal that handles symlink loops and permission errors.
- **SQLite Integration**.
- **Runtime configuration**: Externalized system settings via "config.txt".
