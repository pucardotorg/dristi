exports.formatCaseData = (data) => {
    // Modify the data structure to suit the PDF service requirements
    return {
        ...data,
        formattedField: data.someField.toUpperCase()
    };
};