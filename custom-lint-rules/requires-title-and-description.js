
const hasTitleAndDescription = (params, onError) => {
    const title_line = params.frontMatterLines.filter((line) =>
      line.startsWith("title: ")
    );
    const description_line = params.frontMatterLines.filter((line) =>
        line.startsWith("description: ")
      );
    if (title_line.length < 1) {
      onError({
        lineNumber: 1,
        detail: "Title is not specified",
      });
    } else if (title_line.length > 1) {
        onError({
          lineNumber: 1,
          detail: "Document files are not allowed to specify multiple lists of title",
        });
    } else if (description_line.length < 1) {
      onError({
        lineNumber: 1,
        detail: "Description is not specified",
      });
    } else if (description_line.length > 1) {
        onError({
          lineNumber: 1,
          detail: "Document files are not allowed to specify multiple lists of description",
        });
      }
  };

module.exports = {
    names: ["CMD001", "requires-title-and-description"],
    description: "All documents should specify title and description",
    tags: ["custom"],
    function: hasTitleAndDescription,
  };